package com.mygroup.discountplatform.entities.converters;

import com.mygroup.discountplatform.entities.enums.Genre;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GenreConverter implements AttributeConverter<Genre, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Genre genre) {
        if (genre == null) {
            return null;
        }
        return genre.getCode();
    }

    @Override
    public Genre convertToEntityAttribute(Integer code) {
        return Genre.fromCode(code);
    }
}
