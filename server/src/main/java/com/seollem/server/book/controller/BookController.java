package com.seollem.server.book.controller;

import com.seollem.server.book.dto.BookDto;
import com.seollem.server.book.entity.Book;
import com.seollem.server.book.mapper.BookMapper;
import com.seollem.server.book.service.BookService;
import com.seollem.server.globaldto.MultiResponseDto;
import com.seollem.server.member.entity.Member;
import com.seollem.server.member.service.MemberService;
import com.seollem.server.util.GetEmailFromHeaderTokenUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
@Validated
@SuppressWarnings("unchecked")
public class BookController {

    private final GetEmailFromHeaderTokenUtil getEmailFromHeaderTokenUtil;
    private final MemberService memberService;
    private final BookService bookService;
    private final BookMapper bookMapper;

    public BookController(GetEmailFromHeaderTokenUtil getEmailFromHeaderTokenUtil, MemberService memberService, BookService bookService, BookMapper bookMapper) {
        this.getEmailFromHeaderTokenUtil = getEmailFromHeaderTokenUtil;
        this.memberService = memberService;
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }


    @GetMapping("/library")
    public ResponseEntity getLibrary(@RequestHeader Map<String, Object> requestHeader,
                                     @Positive @RequestParam int page,
                                     @Positive @RequestParam int size){
        String email = getEmailFromHeaderTokenUtil.getEmailFromHeaderToken(requestHeader);
        Member member = memberService.findVerifiedMemberByEmail(email);
        Page<Book> pageBooks = bookService.findVerifiedBooksByMember(page-1, size, member);
        List<Book> books = pageBooks.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(bookMapper.BooksToBooksResponse(books), pageBooks), HttpStatus.OK);
    }


    //책 상세페이지 조회
    @GetMapping("/{book-id}")
    public ResponseEntity getBookDetail(@PathVariable("book-id") long bookId){

        Book book = bookService.findVerifiedBookById(bookId);
        //Book.setMemo(); 구현 필요

        return new ResponseEntity(bookMapper.BookToBookDetailResponse(book),HttpStatus.OK);
    }

    //책 등록
    @PostMapping
    public ResponseEntity postBook(@RequestHeader Map<String, Object> requestHeader,
                                   @Valid @RequestBody BookDto.Post requestBody){
        String email = getEmailFromHeaderTokenUtil.getEmailFromHeaderToken(requestHeader);
        Member member = memberService.findVerifiedMemberByEmail(email);
        Book book = bookMapper.BookPostToBook(requestBody);
        Book verifiedBookStatusBook = bookService.verifyBookStatus(book);
        verifiedBookStatusBook.setMember(member);

        Book createdBook = bookService.createBook(verifiedBookStatusBook);

        return new ResponseEntity(bookMapper.BookToBookPostResponse(createdBook), HttpStatus.CREATED);
    }

    //책 수정
    @PatchMapping("/{book-id}")
    public ResponseEntity patchBook(@PathVariable("book-id") long bookId,
                                    @Valid @RequestBody BookDto.Patch requestBody){
        Book book = bookMapper.BookPatchToBook(requestBody);
        book.setBookId(bookId);
        Book verifiedBookStatusBook = bookService.verifyBookStatus(book);
        Book updatedBook = bookService.updateBook(verifiedBookStatusBook);

        return new ResponseEntity(bookMapper.BookToBookPatchResponse(updatedBook), HttpStatus.OK);
    }
}
