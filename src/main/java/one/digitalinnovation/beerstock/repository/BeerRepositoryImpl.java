package one.digitalinnovation.beerstock.repository;

import one.digitalinnovation.beerstock.entity.Beer;

import java.util.Optional;

public class BeerRepositoryImpl implements BeerRepository {
    @Override
    public Optional<Beer> findByName(String name) {
        return Optional.empty();
    }
}
