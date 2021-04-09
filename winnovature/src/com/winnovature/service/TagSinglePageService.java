package com.winnovature.service;

import java.sql.Connection;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.winnovature.dao.GenerateTagData;
import com.winnovature.dto.TagSinglePageDTO;

public class TagSinglePageService {
	static Logger log = Logger.getLogger(TagSinglePageService.class.getName());

	public static TagSinglePageDTO getTagSignedData(Connection conn, JSONObject jsonRequest) {
		JSONObject tagDataResp = null;
		TagSinglePageDTO tagSingleData = new TagSinglePageDTO();

		tagDataResp = new GenerateTagData().getTagData(jsonRequest.getString("tId"), jsonRequest.getString("tagClassId"),
				jsonRequest.getString("barCode"), conn);

		tagSingleData.setTagId(tagDataResp.getString("tagId"));
		tagSingleData.setUserMemory(tagDataResp.getString("userMemory"));
		tagSingleData.setSignedData(tagDataResp.getString("signature"));
		tagSingleData.settId(jsonRequest.getString("tId"));
		tagSingleData.setBarCode(jsonRequest.getString("barCode"));
		tagSingleData.setTagClassId(jsonRequest.getString("tagClassId"));

		return tagSingleData;
	}

}
