package com.example.OzonHelper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/fbo/supply-order")
public class SupplyOrderController {

    @GetMapping("/list")
    public String getAllSupplyOrders(Model model) {
        return "supply-order-list";
    }
}
