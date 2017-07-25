package com.cirnoteam.accountingassistant.web.controller;

import com.cirnoteam.accountingassistant.web.json.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Yifan on 2017/7/24.
 */
@Controller
@RequestMapping("/get")
public class GetController {

    @ResponseBody
    @RequestMapping("/add/book")
    public Response addBook(HttpServletRequest request) throws IOException {
        return new Response(200);
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
}
