package com.example.OzonHelper.enums.ozon;

import lombok.Getter;

import java.util.Arrays;

public enum AccrualType {
    ACQUIRING("Acquiring", "Эквайринг", 1),
    BACKWARD_SHIPMENT("BackwardShipment", "Обратная магистраль", 2),
    BRAND_COMMISSION("BrandCommission", "Продвижение бренда", 3),
    BRAND_PROMOTION("BrandPromotion", "Подключение продвижения бренда", 4),
    BRAND_SHELF("BrandShelf", "Брендовая полка", 5),
    CANCELLATION("Cancellation", "Обработка отменённых и невостребованных товаров", 6),
    CHARITY("Charity", "Благотворительное пожертвование", 7),
    CLAIM_COMMISSION("ClaimCommission", "Начисления по претензиям", 8),
    CLIENT_RETURN("ClientReturn", "Обработка возвратов", 9),
    COMPENSATION("Compensation", "Компенсация", 10),
    CORRECTION_COMMISSION("CorrectionCommission", "Инвентаризация взаиморасчетов", 11),
    CROSSDOCK("CrossDock", "Кросс-докинг", 12),
    CROSSDOCK_PICKUP_COURIER_DELIVERY("CrossDockPickUpCourierDelivery", "Организация выезда курьера", 13),
    DEFECT_RATE("DefectRate", "Обработка операционных ошибок продавца", 14),
    DISPOSAL("Disposal", "Утилизация", 15),
    DROP_OFF("Drop-Off", "Обработка отправления Drop-off", 16),
    DROP_OFF_AGENT("Drop-Off Agent", "Обработка отправления Drop-off партнёрами", 17),
    EARLY_PAYMENT("EarlyPayment", "Досрочная выплата", 18),
    EXTERNAL_PROMOTION("ExternalPromotion", "Внешнее продвижение", 19),
    FLEXIBLE_PAYMENTS("FlexiblePayments", "Гибкий график выплат", 20),
    FULFILLMENT("Fulfillment", "Сборка заказа", 21),
    INSTALLMENT("Installment", "Рассрочка", 22),
    INTERNET_SITE_ADVERTISING("InternetSiteAdvertising", "Реклама в сети Интернет на Сайте", 23),
    ITEM_CLONING("ItemCloning", "Перенос карточек товаров", 24),
    ITEM_COMPENSATION("ItemCompensation", "Товарная компенсация", 25),
    KAZAKHSTAN_BUYER_INSTALLMENT("KazakhstanBuyerInstallment", "Рассрочка для покупателей из Казахстана", 26),
    LABEL_ORIGINAL("LabelOriginal", "Бейдж Оригинал", 27),
    LAST_MILE("LastMile", "Последняя миля", 28),
    LAST_MILE_COURIER("LastMileCourier", "Доставка до места выдачи", 29),
    LAST_MILE_PICKUP_POINT("LastMilePickUpPoint", "Выдача товара", 30),
    LEAD_GENERATION("LeadGeneration", "Лидогенерация для автодилеров", 31),
    LOGISTIC("Logistic", "Логистика", 32),
    MARKETING("Marketing", "Рекламные услуги", 33),
    MARKING("Marking", "Обязательная маркировка товаров", 34),
    MODERATION("Moderation", "Модерация товаров", 35),
    ORDERS_BOOKING("OrdersBooking", "Привлечение предварительных заказов", 36),
    OZON_DATA("OzonData", "Ozon Data", 37),
    PACKAGE_COST("PackageCost", "Обеспечение материалами для упаковки товара", 38),
    PACKING_FEE("PackingFee", "Упаковка товара партнёрами", 39),
    PARTIAL_RETURN("PartialReturn", "Обработка частичного невыкупа", 40),
    PAY_PER_CLICK("PayPerClick", "Оплата за клик", 41),
    PICKUP("Pick-Up", "Обработка отправления Pick-up", 42),
    PICKUP_COURIER_ARRANGEMENT("PickUpCourierArrangement", "Организация выезда курьера", 43),
    PICKUP_COURIER_DELIVERY("PickUpCourierDelivery", "Доставка курьером Pick-up", 44),
    PICKUP_POINT_RETURN_ACCEPTANCE("PickUpPointReturnAcceptance", "Обработка возвратов, отмен и невыкупов партнёрами", 45),
    PLACEMENTS("Placements", "Размещение товаров на складах Ozon", 46),
    POINTS_FOR_REVIEWS("PointsForReviews", "Баллы за отзывы", 47),
    PREMIUM_CASHBACK_INDIVIDUAL_POINTS("PremiumCashbackIndividualPoints", "Бонусы продавца", 48),
    PREMIUM_CASHBACK_PROMOTION("PremiumCashbackPromotion", "Услуга продвижения Premium", 49),
    PREMIUM_MAILING_COMMISSION("PremiumMailingCommission", "Бонусы продавца - рассылка", 50),
    PREMIUM_MEMBERSHIP("PremiumMembership", "Подписка Premium Pro (процент)", 51),
    PREMIUM_SUBSCRIPTION("PremiumSubscription", "Подписка Premium", 52),
    PREPARING_TO_RETURN("PreparingToReturn", "Подготовка товаров к возврату", 53),
    PROMOTION("Promotion", "Продвижение товара", 54),
    PUSH_CAMPAIGN("PushCampaign", "Рассылка пуш-уведомлений", 55),
    QUANT_PROCESSING_DROP("QuantProcessingDrop", "Обработка и логистика кванта", 56),
    REALIZATION_REPORT_CORRECTION("RealizationReportCorrection", "Корректировка стоимости услуг", 57),
    REPLENISHMENT("Replenishment", "Перемещение товаров между складами Ozon", 58),
    RETURN_FLOW_LOGISTIC("ReturnFlowLogistic", "Обратная логистика", 59),
    RETURN_STORAGE_IN_THE_WAREHOUSE("ReturnStorageInTheWarehouse", "Долгосрочное размещение возврата FBS", 60),
    REVIEW_SPIN("ReviewsPin", "Закрепление отзыва", 61),
    RFBS_CLIENT_DELIVERY_CHARGE("RfbsClientDeliveryCharge", "Перечисление за доставку от покупателя", 62),
    RFBS_DOMESTIC__AGENT_FEE("RfbsDomesticAgentFee", "Агентское вознаграждение Ozon Агрегатор realFBS", 63),
    RFBS_DOMESTIC_DELIVERY("RfbsDomesticDelivery", "Доставка Партнёром Ozon", 64),
    RFBS_EASY_RETURN("RfbsEasyReturn", "Лёгкий возврат", 65),
    RFBS_GLOBAL_AGENT_FEE("RfbsGlobalAgentFee", "Агентское вознаграждение Ozon", 66),
    RFBS_GLOBAL_DELIVERY("RfbsGlobalDelivery", "Услуги международной доставки", 67),
    RFBS_SERVICE_FEE("RfbsServiceFee", "Сервисный сбор за интеграцию с логистической платформой", 68),
    SALE_COMMISSION("SaleCommission", "Вознаграждение за продажу", 69),
    SALE_REVIEW("SaleReview", "Приобретение отзывов на платформе", 70),
    SELLER_RETURNS("SellerReturns", "Вывоз товара со склада силами Ozon", 71),
    SET_OFF("SetOff", "Взаимозачет требований между Договорами", 72),
    SHIPMENT("Shipment", "Магистраль", 73),
    STARS_MEMBERSHIP("StarsMembership", "Звёздные товары", 74),
    STENCIL("Stencil", "Трафареты", 75),
    STOCK_INSURANCE("StockInsurance", "Страхование товара от массовых повреждений", 76),
    SUPPLY_INBOUND("SupplyInbound", "Обработка товара", 77),
    TEMPORARY_PLACEMENT("TemporaryPlacement", "Краткосрочное размещение возврата FBS", 78),
    TEMPORARY_PLACEMENTS_AGENT("TemporaryPlacementsAgent", "Временное размещение товара партнерами", 79),
    VIDEO_COVER("VideoCover", "Генерация видеообложки", 80),
    VOLUME_OBLIGATION_REWARD("VolumeObligationReward", "Бонус за достижение цели продаж", 81),
    VOLUME_WEIGHT_CHARACTERISTICS_PROCESSING("VolumeWeightCharacteristicsProcessing", "Дополнительная обработка ОВХ", 82),
    BRAND_DEPOSIT("BrandDeposit", "Обеспечительные платежи", 83),
    ITEM_PACKING("ItemPacking", "Дополнительная упаковка на складе Ozon", 84),
    ITEM_SEALING("ItemSealing", "Пломбирование товара", 85),
    PACKMAN_CIS_PACKING("PackmanCisPacking", "Дополнительная упаковка товара на ПВЗ в СНГ", 86),
    SOCIAL_MEDIA_ADVERTISING("SocialMediaAdvertising", "Реклама в социальных сетях", 87),
    CLICK_AND_COLLECT("ClickAndCollect", "Самовывоз", 88),
    DEFECT_FINE_MODERATION("DefectFineModeration", "Запрещённый контент", 89),
    DEFECT_FINE_PROHIBITED_GOODS("DefectFineProhibitedGoods", "Запрещённый товар", 90),
    DEFECT_FINE_COUNTERFEIT_GOODS("DefectFineCounterfeitGoods", "Товар с нарушением интеллектуальных прав", 91),
    DEFECT_FINE_COMPLAINT("DefectFineComplaint", "Жалобы покупателей", 92),
    DEFECT_FINE_ERRORS("DefectFineErrors", "Превышение индекса ошибок", 93),
    DEFECT_FINE_SHIPMENT_DELAY_RATE("DefectFineShipmentDelayRate", "Отгрузка в нерекомендованный слот", 94),
    CUSTOMER_REVIEWS("CustomerReviews", "Подписка Управление отзывами", 95),
    ACCELERATED_REVIEW_COLLECTION("AcceleratedReviewCollection", "Ускоренный сбор отзывов", 96),
    PACKAGE_UNIT_PROCESSING("PackageUnitProcessing", "Обработка грузоместа", 97),
    DELIVERY_TO_HANDOVER_PLACE_BY_OZON("DeliveryToHandoverPlaceByOzon", "Доставка до места выдачи силами Ozon", 98),
    INTERNATIONAL_LOGISTIC_DELTA("InternationalLogisticDelta", "Международная логистика", 99),
    OZON_GLOBAL_LOGISTICS_DELIVERY("OzonGlobalLogisticsDelivery", "Транспортно-экспедиционная услуга по организации международной перевозки", 100),
    OVERSIZED_EXTRA_HANDLING("OversizedExtraHandling", "Обработка нестандартного товара", 101),
    B2C_TEMPORARY_PLACEMENT("B2CTemporaryPlacement", "Временное размещение отправления в СЦ/ПВЗ/партнёрами", 102),
    B2C_DISPOSAL("B2CDisposal", "Утилизация отправления", 103),
    B2C_INSURANCE_COMPENSATION("B2CInsuranceCompensation", "Страховое возмещение", 104),
    B2C_INSURANCE_SHIPPING("B2CInsuranceShipping", "Страхование отправления", 105),
    B2C_DROP_OFF("B2C Drop-Off", "B2C Обработка отправления Drop-off", 106),
    B2C_DROP_OFF_AGENT("B2C Drop-Off Agent", "B2C Обработка отправления Drop-off партнёрами", 107),
    B2C_CONTAINER_PACKING("B2CContainerPacking", "Обеспечение материалами для упаковки отправления", 108),
    B2C_CONTAINER_PACKAGE("B2CContainerPackage", "Упаковка отправления партнёрами", 109),
    B2C_COURIER_CLIENT_REINVOICE("B2CCourierClientReinvoice", "B2C Доставка до места выдачи партнёрами", 110),
    B2C_DELIVERY_TO_HANDOVER_PLACE_BY_OZON("B2CDeliveryToHandoverPlaceByOzon", "B2C Доставка до места выдачи силами Ozon", 111),
    B2C_PICKUP_POINT_CLIENT_REINVOICE("B2CPickUpPointClientReinvoice", "B2C Выдача товара партнёрами", 112),
    B2C_PICKUP_POINT_RETURN_ACCEPTANCE("B2CPickUpPointReturnAcceptance", "B2C Обработка возвратов, отмен и невыкупов партнёрами", 113),
    B2C_LOGISTICS("B2CLogistics", "B2C Логистика", 114),
    B2C_BACKWARD_LOGISTICS("B2CBackwardLogistics", "B2C Обратная логистика", 115),
    FIRST_CUSTOMER_REVIEW("FirstCustomerReview", "Сбор первых отзывов", 116),
    INCREASE_ASSORTMENT_LIMIT("IncreaseAssortmentLimit", "Увеличение лимита на создание карточек товаров", 117),
    LABEL_BRAND_VERIFIED("LabelBrandVerified", "Бейдж «Бренд проверен»", 118),
    CUSTOMER_CHAT_POINTS("CustomerChatPoints", "Баллы в чате с покупателем", 119);


    @Getter
    private final String apiValue;
    @Getter
    private final String description;
    private final int id;

    AccrualType(String apiValue, String description, int id) {
        this.apiValue = apiValue;
        this.description = description;
        this.id = id;
    }

    public static AccrualType fromDescription(String value) throws IllegalArgumentException {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Тип начисления не указан");
        }

        String normalizedValue = value.trim().split(":")[0];

        return Arrays.stream(values())
                .filter(accrualType -> accrualType.description.equalsIgnoreCase(normalizedValue))
                .findFirst()
                .orElseThrow((() -> new IllegalArgumentException("Неизвестный тип начисления")));
    }
}
