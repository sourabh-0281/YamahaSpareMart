package com.yamaha.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Parts {
	String imgurl;
	String name;
}
