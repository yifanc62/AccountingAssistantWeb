package com.cirnoteam.accountingassistant.web.controller;

import com.cirnoteam.accountingassistant.web.database.BookUtils;
import com.cirnoteam.accountingassistant.web.database.DbException;
import com.cirnoteam.accountingassistant.web.database.RequestException;
import com.cirnoteam.accountingassistant.web.database.UserUtils;
import com.cirnoteam.accountingassistant.web.json.BookReqEntity;
import com.cirnoteam.accountingassistant.web.json.RemoteIdRespEntity;
import com.cirnoteam.accountingassistant.web.json.Response;
import com.cirnoteam.accountingassistant.web.json.SyncBook;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Yifan on 2017/7/21.
 */

@Controller
@RequestMapping("/sync")
public class SyncController {

    @ResponseBody
    @RequestMapping("/add/book")
    public Response addBook(HttpServletRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BookReqEntity bookReq = mapper.readValue(request.getInputStream(), BookReqEntity.class);
        try {
            if (!bookReq.getType().equals("add")) {
                return new Response(400, "type参数错误！");
            }
            if (UserUtils.verifyToken(bookReq.getToken(), bookReq.getUuid())) {
                RemoteIdRespEntity resp = new RemoteIdRespEntity();
                for (SyncBook book : bookReq.getBooks()) {
                    Long remoteId = BookUtils.addBook(book.getName(), book.getUsername());
                    resp.addIdPair(book.getId(), remoteId);
                }
                return new Response(200).setEntity(resp);
            }
            return new Response(500);
        } catch (DbException e) {
            return new Response(500, e.getMessage());
        } catch (RequestException e) {
            return new Response(400, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/add/account")
    public Response addAccount(HttpServletRequest request) throws IOException {
        return new Response(200);
    }

    @ResponseBody
    @RequestMapping("/add/record")
    public Response addRecord(HttpServletRequest request) throws IOException {
        return new Response(200);
    }

    @ResponseBody
    @RequestMapping("/modify/book")
    public Response modifyBook(HttpServletRequest request) throws IOException {
        return new Response(200);
    }

    @ResponseBody
    @RequestMapping("/modify/account")
    public Response modifyAccount(HttpServletRequest request) throws IOException {
        return new Response(200);
    }

    @ResponseBody
    @RequestMapping("/modify/record")
    public Response modifyRecord(HttpServletRequest request) throws IOException {
        return new Response(200);
    }

    @ResponseBody
    @RequestMapping("/delete/book")
    public Response deleteBook(HttpServletRequest request) throws IOException {
        return new Response(200);
    }

    @ResponseBody
    @RequestMapping("/delete/account")
    public Response deleteAccount(HttpServletRequest request) throws IOException {
        return new Response(200);
    }

    @ResponseBody
    @RequestMapping("/delete/record")
    public Response deleteRecord(HttpServletRequest request) throws IOException {
        return new Response(200);
    }

    @ResponseBody
    @RequestMapping("/get/book")
    public Response getBook(HttpServletRequest request) throws IOException {
        return new Response(200);
    }

    @ResponseBody
    @RequestMapping("/get/account")
    public Response getAccount(HttpServletRequest request) throws IOException {
        return new Response(200);
    }

    @ResponseBody
    @RequestMapping("/get/record")
    public Response getRecord(HttpServletRequest request) throws IOException {
        return new Response(200);
    }
}
