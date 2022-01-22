package com.example.springboot.names;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(NamesController.class)
class NamesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    NamesRepository namesRepository;

    @Test
    void should_return_all_names_when_exist() throws Exception {
        // given
        String name = "michal";
        given(namesRepository.findAll()).willReturn(List.of(name));

        // when
        MvcResult mvcResult = mvc.perform(get("/names").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]", is(name)))
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();

    }

    @Test
    void should_add_name() throws Exception {
        //given
        String name = "adrian";

        MvcResult mvcResult = mvc.perform(post("/names").content(name))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void should_find_by_id() throws Exception {
        //given
        String name = "adrian";
        given(namesRepository.getById(5)).willReturn(name);

        mvc.perform(get("/names/5")) //@PathVariable
//        mvc.perform(get("/names/find?firstLetter=A")) //@PathParam
                .andExpect(status().isOk())
                .andReturn();

    }


    @Test
    void should_find_by_first_letter() throws Exception{
        String maciek = "maciek";
        String firstLetter = "m";

//        given(namesRepository.findAll()).willReturn(new ArrayList<>(List.of(maciek)));

        given(namesRepository.names.stream()
                .filter(name -> name.startsWith(firstLetter))
                .collect(Collectors.toList())).willReturn(new ArrayList<>(List.of(maciek)));

        mvc.perform(get("/names/find?firstLetter=m"))
                .andExpect(status().isOk())
                .andReturn();


    }




}