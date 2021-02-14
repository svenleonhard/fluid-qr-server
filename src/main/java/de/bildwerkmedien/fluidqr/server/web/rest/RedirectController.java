package de.bildwerkmedien.fluidqr.server.web.rest;

import de.bildwerkmedien.fluidqr.server.domain.QrCode;
import de.bildwerkmedien.fluidqr.server.service.QrCodeQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/redirect")
public class RedirectController {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final QrCodeQueryService qrCodeQueryService;

    public RedirectController(QrCodeQueryService qrCodeQueryService) {
        this.qrCodeQueryService = qrCodeQueryService;
    }

    /**
     * {@code GET  /:code} : Forward user to deposited website.
     *
     * @param code the id of the redirection to retrieve.
     */
    @GetMapping("/{code}")
    public void redirectUser(@PathVariable String code, HttpServletResponse httpServletResponse) {
        log.debug("REST request to redirect : {}", code);
        QrCode qrCOde = qrCodeQueryService.findByCode(code);
        if (qrCOde != null && qrCOde.getCurrentRedirect() != null) {
            httpServletResponse.setHeader("Cache-Control", "no-store");
            httpServletResponse.setHeader("Location", qrCodeQueryService.findByCode(code).getCurrentRedirect());
            httpServletResponse.setStatus(302);
        } else {
            httpServletResponse.setStatus(404);
        }

    }
}
