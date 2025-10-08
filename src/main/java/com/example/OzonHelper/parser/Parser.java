package com.example.OzonHelper.parser;

import java.util.List;

public interface Parser<T> {
    List<T> parse(String data);
}
