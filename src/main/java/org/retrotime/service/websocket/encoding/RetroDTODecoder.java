package org.retrotime.service.websocket.encoding;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import org.retrotime.dto.RetroDTO;

import java.io.IOException;

/**
 * Created by vzhemevko on 13.09.15.
 */
public class RetroDTODecoder implements Decoder.Text<RetroDTO> {
    @Override
    public RetroDTO decode(final String jsonMesasge) throws DecodeException {
        ObjectMapper mapper = new ObjectMapper();
        RetroDTO retroDTO = null;
        try {
            retroDTO = mapper.readValue(jsonMesasge, RetroDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retroDTO;
    }

    @Override
    public boolean willDecode(String s) {
        return true; // TODO
    }

    @Override
    public void init(EndpointConfig config) {}

    @Override
    public void destroy() {}
}
