package com.winnovature.service;

import java.sql.Connection;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.winnovature.dao.GenerateTagData;
import com.winnovature.dto.TagSinglePageDTO;

public class TagSinglePageService {
	static Logger log = Logger.getLogger(TagSinglePageService.class.getName());

	public TagSinglePageDTO getTagSignedData(Connection conn, JSONObject jsonRequest) {
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

	public TagSinglePageDTO getTagSummary() {
		
		TagSinglePageDTO tagSummary = new TagSinglePageDTO();
		
		tagSummary.setBarCode("506010-001-4040851");
		tagSummary.setCreatedBy("admin");
		tagSummary.setSignedData("560F8959DFEE7BF55D9BB70648F7B5CBB28C4726E95C36FC9E5D56B8B19FE790");
		tagSummary.setTagClassId("VC4");
		tagSummary.setTagId("34161FA8202F424203ED4498");
		tagSummary.settId("E4161FA82032D69926004851");
		tagSummary.setUserMemory("5858585858585858585858580400560F8959DFEE7BF55D9BB70648F7B5CBB28C4726E95C36FC9E5D56B8B19FE790");
		return tagSummary;
		
	}

}
