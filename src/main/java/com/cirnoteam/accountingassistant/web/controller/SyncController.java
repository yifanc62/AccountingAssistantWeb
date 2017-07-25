package com.cirnoteam.accountingassistant.web.controller;

import com.cirnoteam.accountingassistant.web.database.*;
import com.cirnoteam.accountingassistant.web.json.*;
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
                    Long remoteId = BookUtils.addBook(book);
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
        ObjectMapper mapper = new ObjectMapper();
        AccountReqEntity accountReq = mapper.readValue(request.getInputStream(), AccountReqEntity.class);
        try {
            if (!accountReq.getType().equals("add")) {
                return new Response(400, "type参数错误！");
            }
            if (UserUtils.verifyToken(accountReq.getToken(), accountReq.getUuid())) {
                RemoteIdRespEntity resp = new RemoteIdRespEntity();
                for (SyncAccount account : accountReq.getAccounts()) {
                    Long remoteId = AccountUtils.addAccount(account);
                    resp.addIdPair(account.getId(), remoteId);
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
    @RequestMapping("/add/record")
    public Response addRecord(HttpServletRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        RecordReqEntity recordReq = mapper.readValue(request.getInputStream(), RecordReqEntity.class);
        try {
            if (!recordReq.getType().equals("add")) {
                return new Response(400, "type参数错误！");
            }
            if (UserUtils.verifyToken(recordReq.getToken(), recordReq.getUuid())) {
                RemoteIdRespEntity resp = new RemoteIdRespEntity();
                for (SyncRecord record : recordReq.getRecords()) {
                    Long remoteId = RecordUtils.addRecord(record);
                    resp.addIdPair(record.getId(), remoteId);
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
    @RequestMapping("/modify/book")
    public Response modifyBook(HttpServletRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BookReqEntity bookReq = mapper.readValue(request.getInputStream(), BookReqEntity.class);
        return new Response(200);
    }

    @ResponseBody
    @RequestMapping("/modify/account")
    public Response modifyAccount(HttpServletRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AccountReqEntity accountReq = mapper.readValue(request.getInputStream(), AccountReqEntity.class);
        return new Response(200);
    }

    @ResponseBody
    @RequestMapping("/modify/record")
    public Response modifyRecord(HttpServletRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        RecordReqEntity recordReq = mapper.readValue(request.getInputStream(), RecordReqEntity.class);
        return new Response(200);
    }

    @ResponseBody
    @RequestMapping("/delete/book")
    public Response deleteBook(HttpServletRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BookReqEntity bookReq = mapper.readValue(request.getInputStream(), BookReqEntity.class);
        return new Response(200);
    }

    @ResponseBody
    @RequestMapping("/delete/account")
    public Response deleteAccount(HttpServletRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AccountReqEntity accountReq = mapper.readValue(request.getInputStream(), AccountReqEntity.class);
        return new Response(200);
    }

    @ResponseBody
    @RequestMapping("/delete/record")
    public Response deleteRecord(HttpServletRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        RecordReqEntity recordReq = mapper.readValue(request.getInputStream(), RecordReqEntity.class);
        return new Response(200);
    }
}
