package com.prapthi.crud_demo.Response;


import com.prapthi.crud_demo.dto.CrudDemoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrudResponse {

    private String message;
    private Object data;

}


package com.pcpl.kitchentandem.supplierarticleservice.Supplier.SupplierArticlePrice.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcpl.kitchentandem.pcplsdk.Buyer.BuyerApprovedSupplier.Model.BuyerApprovedSupplier;
import com.pcpl.kitchentandem.pcplsdk.Common.Response.ApplicationResponse;
import com.pcpl.kitchentandem.pcplsdk.Generic.Service.AbstractLazyService;
import com.pcpl.kitchentandem.pcplsdk.Shopping.PurchaseOrderItems.Model.PurchaseOrderItems;
import com.pcpl.kitchentandem.pcplsdk.Supplier.MasterSupplierArticlePrice.Model.MasterSupplierArticlePrice;
import com.pcpl.kitchentandem.pcplsdk.Supplier.SupplierArticleOffers.DTO.SupplierArticleOffersDTO;
import com.pcpl.kitchentandem.pcplsdk.Supplier.SupplierArticlePrice.DTO.SupplierArticlePriceDTO;
import com.pcpl.kitchentandem.pcplsdk.Supplier.SupplierArticlePrice.Model.SupplierArticlePrice;
import com.pcpl.kitchentandem.supplierarticleservice.Buyer.BuyerApprovedSupplier.Service.BuyerApprovedSupplierService;
import com.pcpl.kitchentandem.supplierarticleservice.Config.PCPLConfig;
import com.pcpl.kitchentandem.supplierarticleservice.Shopping.PurchaseOrderItems.Repository.PurchaseOrderItemsRepository;
import com.pcpl.kitchentandem.supplierarticleservice.Supplier.MasterSupplierArticlePrice.Repository.MasterSupplierArticlePriceRepository;
import com.pcpl.kitchentandem.supplierarticleservice.Supplier.MasterSupplierArticlePrice.Service.MasterSupplierArticlePriceService;
import com.pcpl.kitchentandem.supplierarticleservice.Supplier.SupplierArticleOffers.Service.SupplierArticleOffersService;
import com.pcpl.kitchentandem.supplierarticleservice.Supplier.SupplierArticlePrice.Repository.SupplierArticlePriceRepository;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
        import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
        import java.util.stream.Collectors;

@Service
@Slf4j
public class SupplierArticlePriceServiceImpl extends AbstractLazyService<SupplierArticlePrice, SupplierArticlePriceDTO, SupplierArticlePriceRepository> implements SupplierArticlePriceService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PCPLConfig pcplConfig;

    @Autowired
    SupplierArticlePriceRepository supplierArticlePriceRepository;

    @Autowired
    MasterSupplierArticlePriceService masterSupplierArticlePriceService;

    @Autowired
    MasterSupplierArticlePriceRepository masterSupplierArticlePriceRepository;

    @Autowired
    BuyerApprovedSupplierService buyerApprovedSupplierService;

    @Autowired
    @Lazy
    SupplierArticleOffersService supplierArticleOffersService;

    @Autowired
    EntityManager entityManager;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    PurchaseOrderItemsRepository purchaseOrderItemsRepository;

    @Override
    public SupplierArticlePrice getEntityObject() {
        return new SupplierArticlePrice();
    }

    @Override
    public SupplierArticlePriceDTO getDtoObject() {
        return new SupplierArticlePriceDTO();
    }

    @Override
    public String getUniqueConstraintCheckMethodName() {
        return "getSupplierArticleId";
    }

    // -----------------------
    // Helper methods (unchanged logic)
    // -----------------------

    private String emptyToNull(String v) {
        return (v == null || v.trim().isEmpty()) ? null : v;
    }

    private double toDouble(Object o) {
        return o == null ? 0 : Double.parseDouble(o.toString());
    }

    private int toInt(Object o) {
        return o == null ? 0 : Integer.parseInt(o.toString());
    }

    private static class ArticleQueryParams {
        String buyerId;
        int pageNumber;
        int pageSize;
        String sortOrder;
        long offset;
        List<Integer> types;
    }

    private ArticleQueryParams parseQueryParams(Map<String, String> formData) {
        ArticleQueryParams params = new ArticleQueryParams();

        params.buyerId = formData.getOrDefault("buyerId", "");

        params.pageNumber = Integer.parseInt(formData.getOrDefault("current_page", "0"));
        params.pageSize = Integer.parseInt(formData.getOrDefault("page_size", "20"));
        params.sortOrder = formData.getOrDefault("sort_order", "ASC");

        params.offset = params.pageNumber * params.pageSize;

        String types = formData.getOrDefault("types", "1,2,3");

        params.types = Arrays.stream(types.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();

        return params;
    }


    @Override
    public ApplicationResponse syncPending() throws Exception {
        log.trace("Entering syncPending");
        ApplicationResponse applicationResponse = new ApplicationResponse();
        try {
            applicationResponse = this.getPendingSync();

            Map<String, String> formData = new HashMap<>();

            if (applicationResponse.getData() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                ArrayList<?> dataList = (ArrayList<?>) applicationResponse.getData();
                log.debug("Processing {} records from pending sync", dataList.size());

                for (Object supplierArticlePriceObj : dataList) {
                    try {
                        SupplierArticlePrice data;

                        if (supplierArticlePriceObj instanceof LinkedHashMap) {
                            data = objectMapper.convertValue(supplierArticlePriceObj, SupplierArticlePrice.class);
                        } else if (supplierArticlePriceObj instanceof SupplierArticlePrice) {
                            data = (SupplierArticlePrice) supplierArticlePriceObj;
                        } else {
                            log.warn("Unknown data type: {}", supplierArticlePriceObj.getClass().getName());
                            continue;
                        }

                        supplierArticlePriceRepository.save(data);
                        formData.put(data.getId(), data.getId());

                        log.debug("Saved record with ID: {}", data.getId());
                    } catch (Exception e) {
                        log.error("Error processing record: {}", e.getMessage(), e);
                    }
                }

                log.info("Successfully saved {} records", formData.size());
            } else {
                log.warn("No data received from getPendingSync");
            }

            if (!formData.isEmpty()) {
                log.debug("Updating sync status for {} records", formData.size());
                ApplicationResponse updateResponse = this.updateSyncStatus(formData);

                if (!updateResponse.isSuccess()) {
                    log.error("Failed to update sync status: {}", updateResponse.getError());
                    applicationResponse.setSuccess(false);
                    applicationResponse.setError(
                            "Data saved locally but failed to update sync status: " + updateResponse.getError());
                } else {
                    log.info("Successfully updated sync status");
                    applicationResponse.setSuccess(true);
                    applicationResponse.setMessage("Successfully synced " + formData.size() + " records");
                }
            } else {
                log.info("No records to update sync status");
                applicationResponse.setSuccess(true);
                applicationResponse.setMessage("No pending records to sync");
            }

        } catch (Exception e) {
            log.error("Error in syncPending: {}", e.getMessage(), e);
            applicationResponse.setSuccess(false);
            applicationResponse.setError(e.getMessage());
        }

        log.trace("Exiting syncPending");
        return applicationResponse;
    }

    @Override
    public ApplicationResponse getPendingSync() throws Exception {
        log.trace("Entering getPendingSync");
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) auth;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + jwtToken.getToken().getTokenValue());

            HttpEntity<String> entity = new HttpEntity<>("", headers);

            log.debug("Calling common service: {}/kt-supplier/supplier-article-price/get-sync-pending",
                    pcplConfig.getCommonServiceURL());

            ResponseEntity<ApplicationResponse> result = restTemplate.exchange(
                    pcplConfig.getCommonServiceURL() + "/kt-supplier/supplier-article-price/get-sync-pending",
                    HttpMethod.POST,
                    entity,
                    ApplicationResponse.class
            );

            log.trace("Exiting getPendingSync");
            return result.getBody();
        } catch (Exception e) {
            log.error("Error in getPendingSync: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ApplicationResponse updateSyncStatus(Map<String, String> formData) throws Exception {
        log.trace("Entering updateSyncStatus with {} records", formData != null ? formData.size() : 0);
        try {
            if (formData == null || formData.isEmpty()) {
                log.warn("FormData is null or empty in updateSyncStatus");
                ApplicationResponse response = new ApplicationResponse();
                response.setSuccess(false);
                response.setError("No data provided for update");
                return response;
            }

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) auth;

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(formData);

            log.debug("Sending formData: {}", jsonBody);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + jwtToken.getToken().getTokenValue());

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            log.debug("Calling common service: {}/kt-supplier/supplier-article-price/update-sync-status",
                    pcplConfig.getCommonServiceURL());

            ResponseEntity<ApplicationResponse> result = restTemplate.exchange(
                    pcplConfig.getCommonServiceURL() + "/kt-supplier/supplier-article-price/update-sync-status",
                    HttpMethod.POST,
                    entity,
                    ApplicationResponse.class
            );

            log.trace("Exiting updateSyncStatus");
            return result.getBody();
        } catch (Exception e) {
            log.error("Error in updateSyncStatus: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ApplicationResponse populateSupplierArticlePrice() throws Exception {
        ApplicationResponse applicationResponse = new ApplicationResponse();
        int successCount = 0;
        int failureCount = 0;

        try {
            log.info("Starting populateSupplierArticlePrice");

            ApplicationResponse masterSupplierArticlePriceResponse =
                    masterSupplierArticlePriceService.getPendingPrices();

            log.info("getPendingPrices returned: success={}, dataSize={}",
                    masterSupplierArticlePriceResponse.isSuccess(),
                    masterSupplierArticlePriceResponse.getData() != null ?
                            ((List<?>) masterSupplierArticlePriceResponse.getData()).size() : 0
            );

            List<MasterSupplierArticlePrice> masterSupplierArticlePrices =
                    masterSupplierArticlePriceResponse.getData() != null
                            ? (ArrayList<MasterSupplierArticlePrice>) masterSupplierArticlePriceResponse.getData()
                            : Collections.emptyList();

            log.info("Processing {} master price records", masterSupplierArticlePrices.size());

            for (MasterSupplierArticlePrice masterSupplierArticlePrice : masterSupplierArticlePrices) {
                try {
                    log.debug("Processing master price: supplierId={}, articleId={}",
                            masterSupplierArticlePrice.getSupplierId(),
                            masterSupplierArticlePrice.getSupplierArticleId()
                    );

                    SupplierArticlePrice supplierArticlePrice =
                            new SupplierArticlePrice(masterSupplierArticlePrice);

                    supplierArticlePrice = checkDuplicate(supplierArticlePrice);

                    supplierArticlePriceRepository.save(supplierArticlePrice);

                    masterSupplierArticlePrice.setSyncStatus(1);
                    masterSupplierArticlePriceRepository.save(masterSupplierArticlePrice);

                    successCount++;
                    log.debug("Successfully processed record {}/{}", successCount, masterSupplierArticlePrices.size());
                } catch (Exception itemEx) {
                    log.error("Failed to process item: {}", itemEx.getMessage(), itemEx);
                    failureCount++;
                    try {
                        masterSupplierArticlePrice.setSyncStatus(2);
                        masterSupplierArticlePriceRepository.save(masterSupplierArticlePrice);
                    } catch (Exception saveEx) {
                        log.error("Failed to update sync status: {}", saveEx.getMessage(), saveEx);
                    }
                }
            }

            log.info("Populate complete: {} successful, {} failed", successCount, failureCount);

            applicationResponse.setSuccess(true);
            applicationResponse.setMessage(
                    String.format("Populated %d records successfully, %d failed", successCount, failureCount));
            applicationResponse.setData(null);

        } catch (Exception ex) {
            log.error("Failed Populating SAP from master data: {}", ex.getMessage(), ex);
            applicationResponse.setSuccess(false);
            applicationResponse.setError(ex.getMessage());
        }
        return applicationResponse;
    }


    private SupplierArticlePrice checkDuplicate(SupplierArticlePrice supplierArticlePrice) {
        supplierArticlePriceRepository
                .findByBuyerIdAndSupplierArticleId(
                        supplierArticlePrice.getBuyerId() == null ? "" : supplierArticlePrice.getBuyerId(),
                        supplierArticlePrice.getSupplierArticleId()
                )
                .ifPresent(existing -> supplierArticlePrice.setId(existing.getId()));

        return supplierArticlePrice;
    }


    @Override
    public ApplicationResponse populateSupplierArticlePriceForBuyer() throws Exception {

        ApplicationResponse response = new ApplicationResponse();
        int updatedCount = 0;
        int skippedCount = 0;

        try {
            log.info("=== Starting populateSupplierArticlePriceForBuyer ===");

            ApplicationResponse buyerResponse = buyerApprovedSupplierService.findAll();

            if (buyerResponse.getData() == null) {
                log.warn("No BuyerApprovedSupplier data found");
                response.setSuccess(true);
                response.setMessage("No buyer approved suppliers found");
                return response;
            }

            List<BuyerApprovedSupplier> buyerSuppliers =
                    new ObjectMapper().convertValue(
                            buyerResponse.getData(),
                            new com.fasterxml.jackson.core.type.TypeReference<List<BuyerApprovedSupplier>>() {}
                    );

            if (buyerSuppliers.isEmpty()) {
                log.warn("Buyer Approved Suppliers list is empty");
                response.setSuccess(true);
                response.setMessage("No valid buyer approved suppliers found");
                return response;
            }

            log.info("Total Buyer Approved Suppliers: {}", buyerSuppliers.size());

            for (BuyerApprovedSupplier bas : buyerSuppliers) {

                String supplierId = bas.getSupplierId();
                String buyerId = bas.getBuyerId();

                if (supplierId == null || supplierId.isBlank()) {
                    skippedCount++;
                    continue;
                }
                if (buyerId == null || buyerId.isBlank()) {
                    skippedCount++;
                    continue;
                }

                log.info("Processing Supplier={} Buyer={}", supplierId, buyerId);

                int page = 0;
                int pageSize = 100;

                while (true) {

                    Pageable pageable = PageRequest.of(page, pageSize, Sort.by("supplier_article_id"));
                    Page<Object> resultPage =
                            supplierArticlePriceRepository.getUnSetSupplierArticlePricesForTier(
                                    supplierId, buyerId, pageable);

                    if (resultPage == null || resultPage.getContent().isEmpty()) {
                        break;
                    }

                    List<SupplierArticlePrice> prices =
                            transformTupleResultToSupplierArticle(resultPage.getContent());

                    if (prices.isEmpty()) break;

                    List<String> articleIds = prices.stream()
                            .map(SupplierArticlePrice::getSupplierArticleId)
                            .toList();

                    List<SupplierArticleOffersDTO> offers =
                            supplierArticleOffersService.getAllActiveOffers(articleIds);

                    for (SupplierArticlePrice p : prices) {
                        try {
                            p.setBuyerId(buyerId);

                            double multiplier = 1.0;

                            p.setPrice(p.getPrice() * multiplier);
                            p.setRawBasePrice(p.getRawBasePrice() * multiplier);
                            p.setContainerPrice(p.getContainerPrice() * multiplier);
                            p.setRawContainerPrice(p.getRawContainerPrice() * multiplier);

                            SupplierArticleOffersDTO offer = offers.stream()
                                    .filter(o ->
                                            o.getSupplierArticleId().equals(p.getSupplierArticleId()) &&
                                                    (o.getBuyerId() == null || o.getBuyerId().equals(buyerId))
                                    )
                                    .findFirst()
                                    .orElse(null);

                            if (offer != null) {
                                double disc = (offer.getOfferPercentage() / 100.0);
                                p.setRawBasePrice(p.getRawBasePrice() - (p.getRawBasePrice() * disc));
                                p.setRawContainerPrice(p.getRawContainerPrice() - (p.getRawContainerPrice() * disc));
                                p.setValidFrom(offer.getValidFrom());
                                p.setValidUntil(offer.getValidUntil());
                            }

                            supplierArticlePriceRepository.save(p);
                            updatedCount++;

                        } catch (Exception ex) {
                            log.error("Error processing article {}: {}",
                                    p.getSupplierArticleId(), ex.getMessage());
                        }
                    }

                    if (!resultPage.hasNext()) break;
                    page++;
                }
            }

            log.info("=== Completed populateSupplierArticlePriceForBuyer: updated={}, skipped={} ===",
                    updatedCount, skippedCount);

            response.setSuccess(true);
            response.setMessage("Successfully populated " + updatedCount +
                    " supplier article prices. Skipped " + skippedCount + " invalid records.");

        } catch (Exception e) {
            log.error("Fatal Error in populateSupplierArticlePriceForBuyer(): {}", e.getMessage(), e);
            response.setSuccess(false);
            response.setError(e.getMessage());
        }

        return response;
    }

    // ===============================================================
    //              CORRECTED buildFromQuery() OVERLOADS
    // ===============================================================

    /**
     * 1️⃣ ZERO-ARGUMENT VERSION
     * Used in: getArticlesByShoppingList()
     */
    public String buildFromQuery() {
        StringBuilder from = new StringBuilder();

        from.append("(SELECT * FROM kt_supplier_article_price ")
                .append("WHERE buyer_id = :buyerId ")
                .append("AND (valid_from <= now() OR valid_from IS NULL) ")
                .append("AND (valid_until >= now() OR valid_until IS NULL) ")
                .append("AND is_deleted = 0 ")

                .append("UNION ALL ")

                .append("SELECT * FROM kt_supplier_article_price ")
                .append("WHERE (buyer_id = '' OR buyer_id IS NULL) ")
                .append("AND supplier_article_id NOT IN (")
                .append("   SELECT supplier_article_id FROM kt_supplier_article_price ")
                .append("   WHERE buyer_id = :buyerId AND is_deleted = 0")
                .append(") ")
                .append("AND (valid_from <= now() OR valid_from IS NULL) ")
                .append("AND (valid_until >= now() OR valid_until IS NULL) ")
                .append("AND is_deleted = 0 ")

                .append(") AS SAP ");

        return from.toString();
    }

    /**
     * 2️⃣ ONE-ARGUMENT VERSION
     * Used in:
     * - getSupplierArticleBySupplierArticleId()
     * - getSupplierArticleById()
     */
    public String buildFromQuery(String buyerId) {

        StringBuilder from = new StringBuilder();

        from.append("(SELECT * FROM kt_supplier_article_price ")
                .append("WHERE buyer_id = '").append(buyerId).append("' ")
                .append("AND is_deleted = 0 ")
                .append("AND (valid_from <= NOW() OR valid_from IS NULL) ")
                .append("AND (valid_until >= NOW() OR valid_until IS NULL) ")

                .append("UNION ALL ")

                .append("SELECT * FROM kt_supplier_article_price ")
                .append("WHERE (buyer_id = '' OR buyer_id IS NULL) ")
                .append("AND supplier_article_id NOT IN (")
                .append("   SELECT supplier_article_id FROM kt_supplier_article_price WHERE buyer_id='")
                .append(buyerId).append("' AND is_deleted = 0")
                .append(") ")
                .append("AND is_deleted = 0 ")
                .append("AND (valid_from <= NOW() OR valid_from IS NULL) ")
                .append("AND (valid_until >= NOW() OR valid_until IS NULL) ")

                .append(") AS SAP ");

        return from.toString();
    }

    /**
     * 3️⃣ THREE-ARGUMENT VERSION
     * Matches ALL original calls:
     * buildFromQuery(buyerId, "", lang)
     */
    public String buildFromQuery(String buyerId, String searchText, String lang) {

        StringBuilder from = new StringBuilder();

        from.append("(SELECT * FROM kt_supplier_article_price ")
                .append("WHERE buyer_id = '").append(buyerId).append("' ")
                .append("AND is_deleted = 0 ")
                .append("AND (valid_from <= NOW() OR valid_from IS NULL) ")
                .append("AND (valid_until >= NOW() OR valid_until IS NULL) ");

        if (searchText != null && !searchText.trim().isEmpty()) {
            from.append("AND (article_name LIKE '%").append(searchText).append("%' ")
                    .append("OR supplier_article_id LIKE '%").append(searchText).append("%') ");
        }

        from.append("UNION ALL ")

                .append("SELECT * FROM kt_supplier_article_price ")
                .append("WHERE (buyer_id = '' OR buyer_id IS NULL) ")

                .append("AND supplier_article_id NOT IN (")
                .append("   SELECT supplier_article_id FROM kt_supplier_article_price ")
                .append("   WHERE buyer_id='").append(buyerId).append("' AND is_deleted=0")
                .append(") ")

                .append("AND is_deleted = 0 ")
                .append("AND (valid_from <= NOW() OR valid_from IS NULL) ")
                .append("AND (valid_until >= NOW() OR valid_until IS NULL) ");

        from.append(") AS SAP ");

        return from.toString();
    }



    public StringBuilder getBaseQuery() {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(String.join(", ", baseQueryFields));
        sb.append(" FROM ");
        return sb;
    }

    public StringBuilder getBaseQuery(String lang) {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(String.join(", ", baseQueryFields));
        sb.append(" FROM ");
        return sb;
    }

    public StringBuilder getSupplierBaseQuery(String lang) {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(String.join(", ", supplierBaseQueryFields));
        return sb;
    }

    public String buildFromQueryForSupplier(String supplierId, String buyerId, String lang) {

        StringBuilder from = new StringBuilder();

        from.append(" FROM ( ")
                .append("   SELECT * FROM kt_supplier_article_price ")
                .append("   WHERE supplier_id = '").append(supplierId).append("' ")
                .append("   AND (valid_from <= NOW() OR valid_from IS NULL) ")
                .append("   AND (valid_until >= NOW() OR valid_until IS NULL) ")
                .append(" ) AS SAP ");

        return from.toString();
    }

    // ===============================================================
    //                 LAST FOUR WEEK ORDER API
    // ===============================================================

    @Override
    public ApplicationResponse getLastFourWeekOrder(String buyerId, String supplierId, List<String> supplierArticleIds)
            throws Exception {

        ApplicationResponse orderItemsResponse = new ApplicationResponse();
        try {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            String today = simpleDateFormat.format(new Date());
            Date startDate = simpleDateFormat.parse(today);

            Date fromDate = getEndDateFromToday(today, 7);
            Date toDate = getEndDateFromToday(today, 35);

            List<PurchaseOrderItemsDTO> result =
                    purchaseOrderItemsRepository.findAllLastFourWeekOrders(
                            0,
                            buyerId,
                            supplierId,
                            toDate,
                            fromDate,
                            supplierArticleIds
                    );

            orderItemsResponse.setRecordsTotal(result.size());
            orderItemsResponse.setRecordsFiltered(result.size());

            if (!result.isEmpty()) {
                PurchaseOrderItemsDTO orderItems = result.get(0);
                orderItems.setQuantity(0.0);
                orderItemsResponse.setData(orderItems);
            }

            orderItemsResponse.setSuccess(true);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            orderItemsResponse.setSuccess(false);
            orderItemsResponse.setError(ex.getMessage());
        }
        return orderItemsResponse;
    }

    public Date getEndDateFromToday(String today, int days) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        cal.setTime(sdf.parse(today));
        cal.add(Calendar.DATE, -days);

        return sdf.parse(sdf.format(cal.getTime()));
    }


    // ===============================================================
    //                 GET MY ARTICLES
    // ===============================================================

    @Override
    public ApplicationResponse getMyArticles(Map<String, String> formData) throws Exception {

        ApplicationResponse response = new ApplicationResponse();

        try {
            ArticleQueryParams params = parseQueryParams(formData);

            String sql = buildQuery(params);
            String countSql = buildCountQuery(params);

            List<Object[]> rows =
                    entityManager.createNativeQuery(sql).getResultList();

            List<Object> totalCount =
                    entityManager.createNativeQuery(countSql).getResultList();

            long totalElements = 0;

            if (!totalCount.isEmpty()) {
                Object countObj = totalCount.get(0);

                if (countObj instanceof java.math.BigInteger) {
                    totalElements = ((java.math.BigInteger) countObj).longValue();
                } else if (countObj instanceof Number) {
                    totalElements = ((Number) countObj).longValue();
                } else {
                    totalElements = Long.parseLong(countObj.toString());
                }
            }

            int totalPages = params.pageSize == 0 ? 0 :
                    (int) Math.ceil((double) totalElements / params.pageSize);

            List<SupplierArticlePriceDTO> dtoList = transformResult(rows);

            response.setSuccess(true);
            response.setData(dtoList);
            response.setCurrentRecords(dtoList.size());
            response.setRecordsTotal(totalElements);
            response.setRecordsFiltered(totalElements);
            response.setTotalPages(totalPages);

        } catch (Exception ex) {
            log.error("Error in getMyArticles()", ex);
            response.setSuccess(false);
            response.setError(ex.getMessage());
            throw ex;
        }

        return response;
    }


    private List<SupplierArticlePriceDTO> transformResult(List<Object[]> rows) {

        List<SupplierArticlePriceDTO> list = new ArrayList<>();

        for (Object[] r : rows) {

            SupplierArticlePriceDTO dto = new SupplierArticlePriceDTO();

            dto.setId((String) r[0]);
            dto.setSupplierId((String) r[1]);
            dto.setSupplierArticleId((String) r[2]);
            dto.setBuyerId((String) r[3]);

            dto.setPrice(toDouble(r[4]));
            dto.setCurrencyId((String) r[5]);
            dto.setOrderUnit(toDouble(r[6]));
            dto.setMeasurementUnitId((String) r[7]);
            dto.setTaxTypeId((String) r[8]);
            dto.setTaxValue(toDouble(r[9]));

            dto.setValidFrom((Date) r[10]);
            dto.setValidUntil((Date) r[11]);

            dto.setSyncStatus(toInt(r[12]));
            dto.setContainerTypeId((String) r[13]);
            dto.setNoOfUnitsPerContainer(toDouble(r[14]));
            dto.setSaleUnit(toDouble(r[15]));

            dto.setBasePricePerPiece(toDouble(r[16]));
            dto.setContainerPrice(toDouble(r[17]));
            dto.setLeadDays(toInt(r[18]));
            dto.setIsPreorderOnly(toInt(r[19]));

            dto.setRawContainerPrice(toDouble(r[20]));
            dto.setRawNoOfUnitsPerContainer(toDouble(r[21]));
            dto.setRawBasePrice(toDouble(r[22]));

            list.add(dto);
        }

        return list;
    }




    // ===============================================================
    //          GET ARTICLES BY ORDERS (Shopping List)
    // ===============================================================

    @Override
    public ApplicationResponse getArticlesByOrders(Map<String, String> formData) throws Exception {

        ApplicationResponse supplierArticlePriceResponse = new ApplicationResponse();

        String orderIds = formData.get("orderIds") == null ? null : formData.get("orderIds");
        String buyerId = formData.getOrDefault("buyerId", null);
        String lang = formData.getOrDefault("lang", "en");

        String baseQuery = getBaseQuery(lang)
                .append(buildFromQuery(buyerId, "", lang))
                .append(" WHERE SAP.supplier_article_id IN ( ")
                .append("    SELECT supplier_article_id FROM kt_buyer_order_items ")
                .append("    WHERE buyer_id = '").append(buyerId).append("' ")
                .append("    AND buyer_order_id IN (").append(orderIds).append(")")
                .append(" ) ")
                .toString();

        baseQuery += " AND SAP.base_price_per_piece > 0 ORDER BY SAP.base_price_per_piece ASC";

        List<String> orderIdsList =
                Arrays.stream(orderIds.replace("'", "").split(",")).toList();

        List<PurchaseOrderItems> orderItems =
                purchaseOrderItemsRepository.findAllByBuyerIdAndBuyerOrderIdInOrderByCreationTimeDesc(
                        buyerId, orderIdsList
                );

        List<Object> results =
                entityManager.createNativeQuery(baseQuery).getResultList();

        List<SupplierArticlePriceDTO> prices =
                transformTupleResult(results, buyerId, lang);

        for (SupplierArticlePriceDTO price : prices) {
            orderItems.stream()
                    .filter(it -> it.getSupplierArticleId().equalsIgnoreCase(price.getSupplierArticleId()))
                    .findFirst()
                    .ifPresent(it -> price.setQuantity((long) it.getQuantity()));
        }

        supplierArticlePriceResponse.setSuccess(true);
        supplierArticlePriceResponse.setData(prices);
        supplierArticlePriceResponse.setRecordsTotal((long) prices.size());
        supplierArticlePriceResponse.setCurrentRecords(prices.size());
        supplierArticlePriceResponse.setRecordsFiltered((long) prices.size());

        return supplierArticlePriceResponse;
    }




    // ===============================================================
    //          GET ARTICLES BY SHOPPING LIST
    // ===============================================================

    @Override
    public ApplicationResponse getArticlesByShoppingList(Map<String, String> formData) throws Exception {

        ApplicationResponse response = new ApplicationResponse();

        String shoppingListId = formData.getOrDefault("shoppingListId",
                formData.get("shopping_list_id"));

        String buyerId = formData.getOrDefault("buyerId", null);
        String supplierId = formData.getOrDefault("supplierId", null);

        if (shoppingListId == null || shoppingListId.isEmpty()) {
            response.setSuccess(false);
            response.setError("Shopping list ID is required");
            return response;
        }

        if (buyerId == null || buyerId.isEmpty()) {
            response.setSuccess(false);
            response.setError("Buyer ID is required");
            return response;
        }

        try {

            StringBuilder sql =
                    getBaseQuery()
                            .append(buildFromQuery())  // ZERO-ARG overload
                            .append(" WHERE SAP.is_deleted = 0 ")
                            .append(" AND SAP.supplier_article_id IN ( ")
                            .append("       SELECT supplier_article_id FROM kt_shopping_list_article ")
                            .append("       WHERE is_deleted = 0 AND shopping_list_id = :shoppingListId ) ");

            if (supplierId != null && !supplierId.isEmpty()) {
                sql.append(" AND SAP.supplier_id = :supplierId ");
            }

            sql.append(" ORDER BY SAP.base_price_per_piece ASC");

            jakarta.persistence.Query query =
                    entityManager.createNativeQuery(sql.toString());

            query.setParameter("buyerId", buyerId);
            query.setParameter("shoppingListId", shoppingListId);

            if (supplierId != null && !supplierId.isEmpty()) {
                query.setParameter("supplierId", supplierId);
            }

            List<Object[]> rows = query.getResultList();

            List<SupplierArticlePriceDTO> result = new ArrayList<>();

            for (Object row : rows) {
                try {
                    result.add(new SupplierArticlePriceDTO(Arrays.asList((Object[]) row)));
                } catch (Exception e) {
                    log.error("Error converting row to DTO", e);
                }
            }

            response.setSuccess(true);
            response.setData(result);
            response.setRecordsFiltered((long) result.size());
            response.setRecordsTotal((long) result.size());
            response.setCurrentRecords(result.size());

        } catch (Exception ex) {
            log.error("Error loading shopping list articles", ex);
            response.setSuccess(false);
            response.setError(ex.getMessage());
        }

        return response;
    }

    // ===============================================================
    //               GET MY ARTICLES BY SUPPLIER
    // ===============================================================

    @Override
    public ApplicationResponse getMyArticlesBySupplier(Map<String, String> formData) throws Exception {

        ApplicationResponse response = new ApplicationResponse();
        String supplierId = formData.getOrDefault("supplierId", null);
        String buyerId = formData.getOrDefault("buyerId", null);
        String lang = formData.getOrDefault("lang", "en");

        if (supplierId == null || supplierId.isBlank()) {
            response.setSuccess(false);
            response.setError("Supplier ID is required");
            return response;
        }

        ArticleQueryParams params = parseQueryParams(formData);

        StringBuilder sql = getSupplierBaseQuery(lang)
                .append(buildFromQueryForSupplier(supplierId, buyerId, lang))
                .append(" WHERE SAP.is_deleted = 0 ");

        if (buyerId != null && !buyerId.isEmpty()) {
            sql.append(" AND (SAP.buyer_id = '").append(buyerId)
                    .append("' OR SAP.buyer_id IS NULL OR SAP.buyer_id = '') ");
        }

        sql.append(" ORDER BY SAP.base_price_per_piece ASC ");

        List<Object[]> rows = entityManager.createNativeQuery(sql.toString()).getResultList();
        List<SupplierArticlePriceDTO> dtoList = transformResult(rows);

        response.setSuccess(true);
        response.setData(dtoList);
        response.setRecordsFiltered((long) dtoList.size());
        response.setRecordsTotal((long) dtoList.size());
        response.setCurrentRecords(dtoList.size());

        return response;
    }



    // ===============================================================
    //                GET SUPPLIER ARTICLE BY ID
    // ===============================================================

    @Override
    public ApplicationResponse getSupplierArticleById(Map<String, String> formData) throws Exception {

        ApplicationResponse response = new ApplicationResponse();

        String buyerId = formData.getOrDefault("buyerId", null);
        String supplierArticleId = formData.getOrDefault("supplierArticleId", formData.get("supplier_article_id"));
        String lang = formData.getOrDefault("lang", "en");

        if (supplierArticleId == null) {
            response.setSuccess(false);
            response.setError("Supplier Article ID is required");
            return response;
        }

        StringBuilder sql = getBaseQuery(lang)
                .append(buildFromQuery(buyerId, "", lang))
                .append(" WHERE SAP.supplier_article_id = '").append(supplierArticleId).append("' ");

        List<Object[]> rows = entityManager.createNativeQuery(sql.toString()).getResultList();

        List<SupplierArticlePriceDTO> dtoList = new ArrayList<>();

        for (Object row : rows) {
            dtoList.add(new SupplierArticlePriceDTO(Arrays.asList((Object[]) row)));
        }

        response.setSuccess(true);
        response.setData(dtoList);
        response.setRecordsFiltered((long) dtoList.size());
        response.setRecordsTotal((long) dtoList.size());
        response.setCurrentRecords(dtoList.size());

        return response;
    }


    // ===============================================================
    //               GET SUPPLIER ARTICLE BY IDS
    // ===============================================================

    @Override
    public ApplicationResponse getSupplierArticleByIds(Map<String, String> formData) throws Exception {

        ApplicationResponse response = new ApplicationResponse();

        String buyerId = formData.getOrDefault("buyerId", null);
        String lang = formData.getOrDefault("lang", "en");
        String supplierArticleIds = formData.getOrDefault("supplierArticleIds", formData.get("supplier_article_ids"));

        if (supplierArticleIds == null || supplierArticleIds.isEmpty()) {
            response.setSuccess(false);
            response.setError("Supplier Article IDs are required");
            return response;
        }

        StringBuilder sql = getBaseQuery(lang)
                .append(buildFromQuery(buyerId, "", lang))
                .append(" WHERE SAP.supplier_article_id IN (")
                .append(supplierArticleIds)
                .append(") ");

        List<Object[]> rows = entityManager.createNativeQuery(sql.toString()).getResultList();
        List<SupplierArticlePriceDTO> dtoList = new ArrayList<>();

        for (Object row : rows) {
            dtoList.add(new SupplierArticlePriceDTO(Arrays.asList((Object[]) row)));
        }

        response.setSuccess(true);
        response.setData(dtoList);
        response.setRecordsFiltered((long) dtoList.size());
        response.setRecordsTotal((long) dtoList.size());
        response.setCurrentRecords(dtoList.size());

        return response;
    }



    // ===============================================================
    //       GET AVAILABLE SUPPLIER ARTICLES BY IDS
    // ===============================================================

    @Override
    public ApplicationResponse getAvailableSupplierArticleByIds(Map<String, String> formData) throws Exception {

        ApplicationResponse response = new ApplicationResponse();

        String buyerId = formData.getOrDefault("buyerId", null);
        String lang = formData.getOrDefault("lang", "en");
        String supplierArticleIds = formData.getOrDefault("supplierArticleIds", formData.get("supplier_article_ids"));

        if (supplierArticleIds == null || supplierArticleIds.isEmpty()) {
            response.setSuccess(false);
            response.setError("Supplier Article IDs are required");
            return response;
        }

        StringBuilder sql = getBaseQuery(lang)
                .append(buildFromQuery(buyerId, "", lang))
                .append(" WHERE SAP.base_price_per_piece > 0 ")
                .append(" AND SAP.supplier_article_id IN (")
                .append(supplierArticleIds)
                .append(") ")
                .append(" ORDER BY SAP.base_price_per_piece ASC");

        List<Object[]> rows = entityManager.createNativeQuery(sql.toString()).getResultList();

        List<SupplierArticlePriceDTO> dtoList = new ArrayList<>();
        for (Object row : rows) {
            dtoList.add(new SupplierArticlePriceDTO(Arrays.asList((Object[]) row)));
        }

        response.setSuccess(true);
        response.setData(dtoList);
        response.setRecordsFiltered((long) dtoList.size());
        response.setRecordsTotal((long) dtoList.size());
        response.setCurrentRecords(dtoList.size());

        return response;
    }



    // ===============================================================
    //        GET LEAST PRICED SUPPLIER ARTICLE
    // ===============================================================

    @Override
    public ApplicationResponse getLeastPricedSupplierArticle(Map<String, String> formData) throws Exception {

        ApplicationResponse response = new ApplicationResponse();

        String buyerId = formData.getOrDefault("buyerId", null);
        String supplierArticleIds = formData.get("supplier_article_ids");
        String lang = formData.getOrDefault("lang", "en");

        if (supplierArticleIds == null || supplierArticleIds.isEmpty()) {
            response.setSuccess(false);
            response.setError("Supplier Article IDs are required");
            return response;
        }

        StringBuilder sql = getBaseQuery(lang)
                .append(buildFromQuery(buyerId, "", lang))
                .append(" WHERE SAP.base_price_per_piece > 0 ")
                .append(" AND SAP.supplier_article_id IN (")
                .append(supplierArticleIds)
                .append(") ")
                .append(" ORDER BY SAP.base_price_per_piece ASC LIMIT 1 ");

        List<Object[]> rows = entityManager.createNativeQuery(sql.toString()).getResultList();

        if (rows.isEmpty()) {
            response.setSuccess(true);
            response.setData(null);
            return response;
        }

        SupplierArticlePriceDTO dto =
                new SupplierArticlePriceDTO(Arrays.asList(rows.get(0)));

        response.setSuccess(true);
        response.setData(dto);
        response.setRecordsFiltered(1L);
        response.setRecordsTotal(1L);
        response.setCurrentRecords(1);

        return response;
    }



}





}

