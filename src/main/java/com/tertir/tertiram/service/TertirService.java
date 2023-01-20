package com.tertir.tertiram.service;

import com.tertir.tertiram.rest.ApplicationUserModel;
import com.tertir.tertiram.rest.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class TertirService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Initiates session
     *
     * @param login    login
     * @param password password
     * @return authorization token
     * @throws Exception if response code is not 200
     */
    public String initiate(String login, String password) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand("/login");

        try {

            ResponseEntity<?> responseEntity =
                    this.restTemplate.postForEntity(expand, new LoginForm(login, password), ResponseEntity.class);

            return responseEntity.getHeaders().getFirst("Authorization");

        } catch (Exception e) {
            throw new Exception("Սխալ մուտքանուն, կամ գաղտնաբառ։");
        }

    }

    /**
     * Finds all users
     *
     * @param authorizationToken token
     * @return List of users
     * @throws Exception if response code is not 200
     */
    public List<ApplicationUserModel> getAllUsers(String authorizationToken) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand("/tertir/management/getAllUsers");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationToken);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        try {

            return this.restTemplate.exchange(expand,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<ApplicationUserModel>>() {
                    }
            ).getBody();

        } catch (Exception e) {
            throw new Exception("Հնարավոր չէ բեռնել տվյալները։ Փորձեք ավելի ուշ։");
        }

    }

    /**
     * Generates qr codes
     *
     * @param authorizationToken token
     * @param number             qr count
     * @throws Exception if response code is not 200
     */
    public void generateQrCodes(String authorizationToken, int number) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand("/tertir/management/generate/qr/code");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationToken);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        String urlTemplate = UriComponentsBuilder.fromUri(expand)
                .encode().toUriString();
        String format = String.format(urlTemplate + "/%d", number);

        try {
            this.restTemplate.postForObject(format, requestEntity, String.class);
        } catch (Exception e) {
            throw new Exception("Հնարավոր չէ գեներացնել նոր կոդեր։ Փորձեք ավելի ուշ։");
        }

    }

    /**
     * Finds how many Qr codes have been scanned
     *
     * @param authorizationToken token
     * @return scanned Qr codes count
     * @throws Exception if response code is not 200
     */
    public Integer getScannedQrCodesCount(String authorizationToken) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand("/tertir/management/soldPapersCount");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationToken);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        try {
            return this.restTemplate.exchange(expand, HttpMethod.GET, requestEntity, Integer.class).getBody();
        } catch (Exception e) {
            throw new Exception("Հնարավոր չէ պարզել սկանավորված կոդերի քանակը։ Փորձեք ավելի ուշ։");
        }
    }

    /**
     * Generates pdf file from Qr code images
     *
     * @param authorizationToken token
     * @return byte[]
     * @throws Exception if response code is not 200
     */
    public byte[] getAllNotPrintedQrCodes(String authorizationToken) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand("/tertir/management/getAllNotPrintedQrCodes");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationToken);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        try {
            return this.restTemplate.exchange(expand,
                    HttpMethod.GET,
                    requestEntity,
                    byte[].class).getBody();
        } catch (Exception e) {
            throw new Exception("Հնարավոր չէ ստանալ PDF ֆայլը։ Փորձեք ավելի ուշ։");
        }

    }

    /**
     * Purchase user
     *
     * @param id id
     */
    public void purchaseUser(String id, String authorizationToken) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand("/tertir/management/purchaseUser");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationToken);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        String urlTemplate = UriComponentsBuilder.fromUri(expand)
                .encode().toUriString();
        String format = String.format(urlTemplate + "/%s", id);

        try {
            this.restTemplate.put(format, requestEntity);
        } catch (Exception e) {
            throw new Exception("Հնարավոր չէ վճարել հաճախորդին։ Փորձեք ավելի ուշ։");
        }
    }

    public void changePassword(String oldPassword, String newPassword, String authorizationToken) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand("/tertir/management/changePassword");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationToken);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        String urlTemplate = UriComponentsBuilder.fromUri(expand)
                .queryParam("oldPassword",oldPassword)
                .queryParam("newPassword",newPassword)
                .encode().toUriString();

        try {
            this.restTemplate.put(urlTemplate, requestEntity);
        } catch (Exception e) {
            throw new Exception(String.format("Հնարավոր չէ փոխել գաղտնաբառը։ Փորձեք ավելի ուշ։ %s",e.getMessage()));
        }
    }
}
