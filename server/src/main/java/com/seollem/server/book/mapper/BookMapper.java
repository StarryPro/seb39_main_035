package com.seollem.server.book.mapper;

import com.seollem.server.book.dto.BookDto;
import com.seollem.server.book.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

    Book BookPostToBook(BookDto.Post requestBody);

    BookDto.DetailResponse BookToBookDetailResponse(Book book);

    BookDto.PostResponse BookToBookPostResponse(Book book);
}
