package org.retrotime.service;

import org.retrotime.model.Release;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by vzhemevko on 5/23/2015.
 */
@Service
@Transactional
public class ReleaseServiceImpl implements  ReleaseService {

    @Override
    public void saveRelease(Release release) {
        // TODO
    }
}
