package one.digitalinnovation.beerstock.service;

import one.digitalinnovation.beerstock.builder.BeerDTOBuilder;
import one.digitalinnovation.beerstock.entity.Beer;
import one.digitalinnovation.beerstock.repository.BeerRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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
        void whenNotRegisteredBeerNameIsGivenThenThrowAnException() throws BeerNotFoundException {
            //given
            BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

            //when
            when(beerRepository.findByName(expectedFoundBeer.getName())).thenReturn(Optional.empty());

            //then
            assertThrows(BeerNotFoundException.class, () -> bearService.findByName(expectedFoundBeerDTO.getName()));
        }

        @Test
        void whenListBeerIsCalledThenReturnAListOfBeers() {
            //given
            BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
            Beer expectedFoundBeer = beerMapper.toModel(expectedFoundBeerDTO);

            //when
            when(beerRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundBeer));

            //then
            List<BeerDTO> foundListBeersDTO = beerService.listAll();

            assertThat(foundListBeersDTO, is(not(empty))));
            assertThat(foundListBeersDTO.get(0), is(equalTo(expectedFoundBeerDTO)));
    }

        @Test
        void whenListBeerIsCalledThenReturnAEmptyListOfBeers() {
            //given
            BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
            Beer expectedFoundBeer = beerMapper.toModel(expectedFoundBeerDTO);

            //when
            when(beerRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

            //then
            List<BeerDTO> foundListBeersDTO = beerService.listAll();

            assertThat(foundListBeersDTO, is(empty)));
        }

        @Test
        void whenExclusionIsCalledWithValidIdThenABeerShouldBeDeleted() throws BeerNotFoundException {
            //given
            BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
            Beer expectedFoundBeer = toModel(expectedFoundBeerDTO);

            //when
            when(beerRepository.findById(expectedDeletedBeerDTO.getId())).thenReturn(Optional.of(expectedDeletedBeer));
            doNothing().when(beerRepository).deleteById(expectedDeletedBeerDTO.getId());

            //then
            beerService.deletedById(expectedDeletedBeerDTO.getId());

            verify(beerRepository, times(wantedNumberOfInvocations: 1)).findById(expectedDeletedBeerDTO.getId());
            verify(beerRepository, times(wantedNumberOfInvocations: 1)).deleteById(expectedDeletedBeerDTO.getId());
        }

        @Test
        void whenIncrementIsCalledThenIncrementBeerStock() throws BeerNotFoundException, BeerStockExceededException {
            //given
            BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
            Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);

            //when
            when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
            when(beerRepository.save(expectedBeer)).thenReturn(expectedBeer);

            int quantityToIncrement = 10;
            int expectedQuantityAfterIncrement = expectedBeerDTO.getQuantity() + quantityToIncrement;

            // then
            BeerDTO incrementedBeerDTO = beerService.increment(expectedBeerDTO.getId(), quantityToIncrement);

            assertThat(expectedQuantityAfterIncrement, equalTo(incrementedBeerDTO.getQuantity()));
            assertThat(expectedQuantityAfterIncrement, lessThan(expectedBeerDTO.getMax()));
        }

        @Test
        void whenIncrementIsGreatherThanMaxThenThrowException() {
            BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
            Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);

            when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));

            int quantityToIncrement = 80;
            assertThrows(BeerStockExceededException.class, () -> beerService.increment(expectedBeerDTO.getId(), quantityToIncrement));
        }

        @Test
        void whenIncrementAfterSumIsGreatherThanMaxThenThrowException() {
            BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
            Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);

            when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));

            int quantityToIncrement = 45;
            assertThrows(BeerStockExceededException.class, () -> beerService.increment(expectedBeerDTO.getId(), quantityToIncrement));
        }

        @Test
        void whenIncrementIsCalledWithInvalidIdThenThrowException() {
            int quantityToIncrement = 10;

            when(beerRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());

            assertThrows(BeerNotFoundException.class, () -> beerService.increment(INVALID_BEER_ID, quantityToIncrement));
        }
    }

}

