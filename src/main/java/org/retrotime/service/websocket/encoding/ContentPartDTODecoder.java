package org.retrotime.service.websocket.encoding;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import org.retrotime.dto.ContentPartDTO;

import java.io.IOException;

/**
 * Created by vzhemevko on 1.10.15.
 */
public class ContentPartDTODecoder implements Decoder.Text<ContentPartDTO> {

    @Override
    public void init(EndpointConfig config) {}

    @Override
    public void destroy() {}

    @Override
    public ContentPartDTO decode(final String jsonMesasge) throws DecodeException {
        ObjectMapper mapper = new ObjectMapper();
        ContentPartDTO part = new ContentPartDTO();
        try {
            part = mapper.readValue(jsonMesasge, ContentPartDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return part;
    }

    @Override
    public boolean willDecode(String s) {
        // TODO
        return true;
    }

}
