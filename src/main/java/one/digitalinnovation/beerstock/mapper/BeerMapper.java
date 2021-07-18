package one.digitalinnovation.beerstock.mapper;


import one.digitalinnovation.beerstock.entity.Beer;
import one.digitalinnovation.beerstock.service.BeerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public @interface BeerMapper {
    BeerMapper INSTANCE = Mappers.getMapper(BeerMapper.class);


    Beer toModel(BeerDTO beerDTO);
    Beer toDTO(Beer beer);
}