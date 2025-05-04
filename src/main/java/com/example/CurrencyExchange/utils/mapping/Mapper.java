package com.example.CurrencyExchange.utils.mapping;

public interface Mapper<T, K> {
    T fromDTOToEntity(K from);
    K fromEntityToDTO(T from);
}
