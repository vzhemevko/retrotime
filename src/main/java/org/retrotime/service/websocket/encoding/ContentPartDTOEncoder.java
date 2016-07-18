package org.retrotime.service.websocket.encoding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import org.retrotime.dto.ContentPartDTO;

/**
 * Created by vzhemevko on 1.10.15.
 */
public class ContentPartDTOEncoder implements Encoder.Text<ContentPartDTO>{

    @Override
    public void init(EndpointConfig config) {}

    @Override
    public void destroy() {}

    @Override
    public String encode(ContentPartDTO part) throws EncodeException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(part);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null; // TODO
    }

}
