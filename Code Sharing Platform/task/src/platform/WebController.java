package platform;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/code")
public class WebController {

    private final CodeService codeService;

    public WebController(CodeService codeService) {
        this.codeService = codeService;
    }

    @GetMapping("/{uuid}")
    public String getCodeAsHTML(@PathVariable String uuid, Model model) {
        Code code = codeService.findCodeByUUID(uuid);
        model.addAttribute("isViewRestricted", code.getIsViewRestricted());
        model.addAttribute("isTimeRestricted", code.getIsTimeRestricted());
        codeService.updateRestrictionData(code);
        if (code.getIsRestricted()) {
            model.addAttribute("code", code.getCode());
            model.addAttribute("date", code.getDate());
            model.addAttribute("views", code.getViews());
            model.addAttribute("time", code.getTime());
            return "codeRestricted";
        } else {
            model.addAttribute("code", code.getCode());
            model.addAttribute("date", code.getDate());
            return "codeNonRestricted";
        }
    }

    @GetMapping("/latest")
    public String getLatestCodesAsHTML(Model model) {
        List<Code> codes = codeService.findLatest10Codes();

        model.addAttribute("codes", codes);
        model.addAttribute("newCode", new Code());

        return "codeList";
    }

    @GetMapping(value = "/new")
    public String openFormForAddingCode() {
        return "form";
    }

    @PostMapping(value = "/post")
    public String addCode(@ModelAttribute Code newCode) {
        codeService.save(newCode);
        return "redirect:/code/latest";
    }
}
