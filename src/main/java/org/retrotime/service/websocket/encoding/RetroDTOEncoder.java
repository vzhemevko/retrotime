package org.retrotime.service.websocket.encoding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import org.retrotime.dto.RetroDTO;

/**
 * Created by vzhemevko on 13.09.15.
 */
public class RetroDTOEncoder implements Encoder.Text<RetroDTO> {
    @Override
    public String encode(RetroDTO retroDTO) throws EncodeException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(retroDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null; // TODO
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
