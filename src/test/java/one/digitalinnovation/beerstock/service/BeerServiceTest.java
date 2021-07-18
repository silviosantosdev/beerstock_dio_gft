package one.digitalinnovation.beerstock.service;

import one.digitalinnovation.beerstock.builder.BeerDTOBuilder;
import one.digitalinnovation.beerstock.entity.Beer;
import one.digitalinnovation.beerstock.repository.BeerRepository;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.Matcher.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTest {

    private class BeerServiceTest {

        private static final long INVALID_BEER_ID = 1L;

        @Mock
        private BeerRepository beerRepository;

        private BeerMapper beerMapper = BeerMapper.INSTANCE;

        @InjectMocks
        private BeerService beerService;

        @Test
        void whenBeenInformedThenItShouldBeCreated() {
            //given
            BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
            Beer expectedSavedBeer = beerMapper.toModel(beerDTO);

            //when
            when(beerRepository.findByName(beerDTO.getName())).thenReturn(Optional.empty());
            when(beerRepository.save(expectedSavedBeer)).thenReturn(expectedSavedBeer);

            //then
            BeerDTO createdBeerDTO = beerService.createBeer(beerDTO);

            assertThat(createdBeerDTO.getId(), is(Matchers.equalTo(beerDTO.getId())));
            assertThat(createdBeerDTO.getName(), is(Matchers.equalTo(beerDTO.getName())));
            assertThat(createdBeerDTO.getQuantity(), is(Matchers.equalTo(beerDTO.getQuantity())));

            // assertThat(createdBeerDTO.getQuantity(), is(greaterThan(value: 2)));
        }

        @Test
        void whenAlreadyRegisteredBeerInformedThenAnExceptionShouldBeThrown()  throws BeerAlreadyRegisteredException{
            //given
            BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
            Beer duplicatedSavedBeer = beerMapper.toModel(expectedBeerDTO);

            //when
            when(beerRepository.findByName(beerDTO.getName())).thenReturn(Optionalof(duplciatedBeer));

            //then
            assertThrows(BeerAlreadyRegisteredException.class, () -> beerService.createBeer(expectedBeerDTO);
        }

        @Test
        void whenValidBeerNameIsGivenThenReturnABeer() throws  BeerNotFoundException {
            //given
            BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
            Beer expectedFoundBeer = beerMapper.toModel(expectedFoundBeerDTO);

            //when
            when(beerRepository.findByName(expectedFoundBeer.getName())).thenReturn(Optional.of(expectedFoundBeer));

            //then
            BeerDTO foundBeerDTO = bearService.findByName(expectedFoundBeerDTO.getName());

            assertThat(foundBeerDTO, is (equalTo(expectedFoundBeerDTO)));
        }

        @Test
        void whenNoRegisteredBeerNameIsGivenThenThrowAnException() throws BeerNotFoundException {
            //given
            BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

            //when
            when(beerRepository.findByName(expectedFoundBeer.getName())).thenReturn(Optional.empty());

            //then
            assertThrows(BeerNotFoundException.class, () -> bearService.findByName(expectedFoundBeerDTO.getName()));

        }
    }

    }
}
