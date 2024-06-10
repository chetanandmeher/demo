package com.example.demo.rough;

import jakarta.servlet.ServletOutputStream;
import org.springframework.http.converter.json.GsonBuilderUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class twoListForLoop {
    List<Character> chars = Arrays.asList('a', 'b', 'c', 'd');
    List<Integer> integers = Arrays.asList(1, 2, 3, 4);

    Stream<Character> characterStream = chars.stream();
    Stream<Integer> integerStream = integers.stream();

    Stream.concat()

}
