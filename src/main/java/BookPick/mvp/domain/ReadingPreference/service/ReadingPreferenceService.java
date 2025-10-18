package BookPick.mvp.domain.ReadingPreference.service;

import BookPick.mvp.domain.ReadingPreference.dto.ReadingPreferenceDtos.*;
import BookPick.mvp.domain.ReadingPreference.repository.ReadingPreferenceRepository;
import BookPick.mvp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadingPreferenceService {
    private final ReadingPreferenceRepository readingPreferenceRepository ;
    private final UserRepository userRepository;

    @Transactional
    public  ReadingPreferenceRegisterRes create(Long userId, ReadingPreferenceRegisterReq req) {

        return new ReadingPreferenceRegisterRes(1L);
    }
}
