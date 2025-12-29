package BookPick.mvp.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PagenationService {


    public int changeMinusPageToZeroPage(int page){
        if(page < 0){
            return 0;
        }
        return page;
    }
}
