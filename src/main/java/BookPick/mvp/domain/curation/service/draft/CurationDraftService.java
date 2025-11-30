package BookPick.mvp.domain.curation.service.draft;

import BookPick.mvp.domain.curation.dto.base.CurationReq;
import BookPick.mvp.domain.curation.dto.base.CurationRes;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.common.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurationDraftService {
    private final CurationRepository curationRepository;
    private final UserRepository userRepository;

    public CurationRes draftSave(Long userId, CurationReq req){
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Curation draftedCuration = Curation.createDraft(user, req);
        Curation saved =  curationRepository.save(draftedCuration);

        return CurationRes.from(saved);
        }
    }


