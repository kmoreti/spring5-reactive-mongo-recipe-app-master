package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.ui.Model;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by jt on 6/17/17.
 */
@Ignore
@WebFluxTest
public class IndexControllerTest {

    @MockBean
    RecipeService recipeService;

    @Mock
    Model model;

    @Autowired
    IndexController controller;

    WebTestClient webTestClient;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        controller = new IndexController(recipeService);

        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    public void testMockMVC() {
//        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        when(recipeService.getRecipes()).thenReturn(Flux.empty());

//        mockMvc.perform(get("/"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("index"));
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody();

    }

    @Test
    public void getIndexPage() {

        //given
        final Set<Recipe> recipes = new HashSet<>();
        recipes.add(new Recipe());

        final Recipe recipe = new Recipe();
        recipe.setId("1");

        recipes.add(recipe);

        when(recipeService.getRecipes()).thenReturn(Flux.fromIterable(recipes));

        final ArgumentCaptor<List<Recipe>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        //when
        final String viewName = controller.getIndexPage(model);


        //then
        assertEquals("index", viewName);
        verify(recipeService, times(1)).getRecipes();
        verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
//        final Flux<Recipe> fluxInController = argumentCaptor.getValue();
        final List<Recipe> recipeList = argumentCaptor.getValue();
        assertEquals(2, recipeList.size());
    }

}