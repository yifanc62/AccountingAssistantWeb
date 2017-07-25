package com.cirnoteam.accountingassistant.web.controller;

import com.cirnoteam.accountingassistant.web.database.DbException;
import com.cirnoteam.accountingassistant.web.database.DirtyUtils;
import com.cirnoteam.accountingassistant.web.database.RequestException;
import com.cirnoteam.accountingassistant.web.database.UserUtils;
import com.cirnoteam.accountingassistant.web.json.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by Yifan on 2017/7/24.
 */
@Controller
@RequestMapping("/get")
public class GetController {

    @ResponseBody
    @RequestMapping("/add/book")
    public Response addBook(@RequestParam String uuid, @RequestParam String token) throws IOException {
        BookReqEntity entity = new BookReqEntity();
        try {
            if (UserUtils.verifyToken(token, uuid)) {
                List<SyncBook> books = DirtyUtils.getAllNonDeleteBooks(uuid);
                entity.setBooks(books);
                return new Response(200).setEntity(entity);
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
    public Response addAccount(@RequestParam String uuid, @RequestParam String token) throws IOException {
        AccountReqEntity entity = new AccountReqEntity();
        try {
            if (UserUtils.verifyToken(token, uuid)) {
                List<SyncAccount> accounts = DirtyUtils.getAllNonDeleteAccounts(uuid);
                entity.setAccounts(accounts);
                return new Response(200).setEntity(entity);
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
    public Response addRecord(@RequestParam String uuid, @RequestParam String token) throws IOException {
        RecordReqEntity entity = new RecordReqEntity();
        try {
            if (UserUtils.verifyToken(token, uuid)) {
                List<SyncRecord> records = DirtyUtils.getAllNonDeleteRecords(uuid);
                entity.setRecords(records);
                return new Response(200).setEntity(entity);
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
}
