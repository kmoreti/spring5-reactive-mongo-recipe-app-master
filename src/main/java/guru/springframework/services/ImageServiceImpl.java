package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * Created by jt on 7/3/17.
 */
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {


    private final RecipeReactiveRepository recipeReactiveRepository;

    public ImageServiceImpl(final RecipeReactiveRepository recipeReactiveRepository) {

        this.recipeReactiveRepository = recipeReactiveRepository;
    }

    @Override
    public Mono<Void> saveImageFile(final String recipeId, final MultipartFile file) {

        final Recipe recipeBlock = recipeReactiveRepository.findById(recipeId)
                .map(recipe -> {
                    try {
                        final Byte[] byteObjects = new Byte[file.getBytes().length];

                        int i = 0;

                        for (final byte b : file.getBytes()) {
                            byteObjects[i++] = b;
                        }

                        recipe.setImage(byteObjects);

                        return recipe;

                    } catch (final IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException();
                    }
                }).block();
        recipeReactiveRepository.save(recipeBlock).block();

        return Mono.empty();
    }
}