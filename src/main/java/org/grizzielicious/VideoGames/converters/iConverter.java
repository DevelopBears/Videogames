package org.grizzielicious.VideoGames.converters;

import java.util.List;

public interface iConverter<D, E> {
    public E convertFromDto (D dto);
    public D convertFromEntity (E entity);
    public default List<E> convertFromDtoList(List<D> dtoList) {
        return dtoList.stream()
                .map(this::convertFromDto)
                .toList();
    }
    public default List<D> convertFromEntityList (List<E> entityList) {
        return entityList.stream()
                .map(this::convertFromEntity)
                .toList();
    }
}
