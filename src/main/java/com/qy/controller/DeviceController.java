package com.qy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/dev")
public class DeviceController {
	private static  Logger log = LoggerFactory.getLogger(DeviceController.class);  
	
}
