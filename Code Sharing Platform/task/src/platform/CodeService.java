package platform;

import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CodeService {
    private final CodeRepository codeRepository;

    public CodeService(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    public Code findCodeByUUID(String uuid) {
        return codeRepository.findByUuid(uuid).orElseThrow(CodeNotFoundException::new);
    }

    public void updateRestrictionData(Code code) {
        cutNumberOfViews(code);
        cutElapsedTime(code);
        checkIfCodeIsValid(code);
        save(code);
    }

    private void cutNumberOfViews(Code code) {
        if (code.getViews() >= 0 && code.getIsViewRestricted()) {
            code.setViews(code.getViews() - 1);
        }
    }

    private void cutElapsedTime(Code code) {
        int secondOfDay = LocalTime.now().toSecondOfDay();
        int leftTime = code.getDurationTime().toSecondOfDay() - secondOfDay;
        if (code.getIsTimeRestricted())
            code.setTime(Math.max(leftTime, 0));
    }

    private void checkIfCodeIsValid(Code code) {
        if ((code.getViews() < 0 && code.getIsViewRestricted()) || (code.getTime() <= 0 && code.getIsTimeRestricted())) {
            codeRepository.delete(code);
            throw new CodeNotFoundException();
        }
    }

    public List<Code> findLatest10Codes() {
        return codeRepository.findAll()
                .stream()
                .filter(code -> !code.getIsRestricted())
                .sorted((c1, c2) -> c2.getId().compareTo(c1.getId()))
                .limit(10)
                .collect(Collectors.toList());
    }

    public void save(Code code) {
        code.setDurationTime(code.getTime());
        code.setTimeRestricted();
        code.setViewRestricted();
        code.setRestricted();
        codeRepository.save(code);
    }

    public void deleteAll() {
        codeRepository.deleteAll();
    }

}
