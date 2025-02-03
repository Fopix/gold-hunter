//сворачивание / разворачивание списка игр
let gameList = $(".game-item");
let gameListHide = gameList.slice(15, gameList.length - 1);
let gameListBtn = $("#game-list-btn a");

gameListHide.hide();

gameListBtn.click(function () {
    if (gameListBtn.text() === "Развернуть") {
        gameListHide.show(400);
        gameListBtn.text("Свернуть");
    } else {
        gameListHide.hide(400);
        gameListBtn.text("Развернуть");
    }
});

//сворачиваемая информация
$("#product-question-1").click(function () {
    $("#question-p-1").slideToggle();
    return false;
});

$("#product-question-2").click(function () {
    $("#question-p-2").slideToggle();
    return false;
});

$("#order-question-1").click(function () {
    $("#question-o-1").slideToggle();
    return false;
});

$("#order-question-2").click(function () {
    $("#question-o-2").slideToggle();
    return false;
});

//мобильное меню
(function () {
    'use strict';

    class Menu {
        constructor(settings) {
            this.nodeMenu = settings.nodeMenu;
            settings.nodeMenuButton.addEventListener('click', this.toggle.bind(this));
        }

        toggle() {
            return this.nodeMenu.classList.toggle('js-menu_activated'),
                $('body').attr('style', '');
        }
    }

    let nodeMenu = document.querySelector('body');

    new Menu({
        nodeMenu: nodeMenu,
        nodeMenuButton: nodeMenu.querySelector('.js-menu__toggle')
    });
})();

//ВАЛИДАЦИЯ

//обработчик data атрибутов
let rate = $("#server option:selected").data("price");
$("#server").on('change', function () {
    rate = $("#server option:selected").data("price");

    let paymentToAmount = payment.val() / rate;
    amount.val(paymentToAmount.toFixed(2));

    minSum();
    sumAndRateCalc();
});

let paymentCommission = $("#paymentMethod option:selected").data("commission");
$("#paymentMethod").on('change', function () {
    paymentCommission = $("#paymentMethod option:selected").data("commission");
    sumAndRateCalc();
});

//заполнение суммы и количества
function autoFillInputs() {
    let sumForInput = $("#commission").text();

    if (sumForInput != 0.00) {
        let a = paymentCommission / 100;
        let b = a + 1;

        let sumFIFixed = sumForInput / b + a;
        $("#payment").val(sumFIFixed.toFixed(2));
        let amountFIFixed = $("#payment").val() / rate;
        $("#amount").val(amountFIFixed.toFixed(2));
    }
}

autoFillInputs();

//генерация минимального количества для покупки
let amount = $('#amount');

function minSum() {
    let minSum = Math.ceil(300 / rate);
    amount.attr("min", minSum);
}

minSum();

//обработка формы в карточки
let payment = $('#payment');
let commission = $('#commission');
let allowances = $('#allowances');

function sumAndRateCalc() {
    let paymentValue = payment.val();
    let sumWithCommission = parseFloat(paymentValue) + parseFloat(paymentValue * paymentCommission / 100);
    let rateForAllowances = rate;

    if (paymentValue >= 4000) {
        rateForAllowances = rateForAllowances - rateForAllowances * 7.5 / 100;
    } else if (paymentValue >= 2500) {
        rateForAllowances = rateForAllowances - rateForAllowances * 5 / 100;
    } else if (paymentValue >= 1500) {
        rateForAllowances = rateForAllowances - rateForAllowances * 3 / 100;
    } else if (paymentValue >= 1000) {
        rateForAllowances = rateForAllowances - rateForAllowances * 2 / 100;
    } else if (paymentValue >= 500) {
        rateForAllowances = rateForAllowances - rateForAllowances / 100;
    }

    let allowancesForAmount = paymentValue / rateForAllowances;


    if (sumWithCommission) {
        commission.text(sumWithCommission.toFixed(2).replace(/(\d{1,3}(?=(?:\d\d\d)+(?!\d)))/g, "$1" + ' '));
        allowances.text(allowancesForAmount.toFixed(2).replace(/(\d{1,3}(?=(?:\d\d\d)+(?!\d)))/g, "$1" + ' '));
    } else {
        commission.text("0.00");
        allowances.text("0.00");
    }
}

payment.bind('input', function () {
    let paymentToAmount = payment.val() / rate;

    amount.val(paymentToAmount.toFixed(2));
    sumAndRateCalc();
});

amount.bind('input', function () {
    let amountToPayment = amount.val() * rate;

    payment.val(amountToPayment.toFixed(2));
    sumAndRateCalc();
});


//модальное окно
let body = $('body');
let modalBg = $('.modal-bg');

$("#btnReady").click(function () {
    if (!$("#btnReady").hasClass("btn-deactive")) {
        modalBg.css('display', 'flex');
        body.css('overflow', 'hidden');
        //смена кнопок
        $('#btnContinue1').css('display', 'flex');
        $('#btnContinue2').css('display', 'none');
    }
});

$("#btnCompleted").click(function () {
    if (!$("#btnCompleted").hasClass("btn-deactive")) {
        modalBg.css('display', 'flex');
        body.css('overflow', 'hidden');
        //смена кнопок
        $('#btnContinue1').css('display', 'none');
        $('#btnContinue2').css('display', 'flex');
    }
});

$(".btn-close").click(function () {
    modalBg.css('display', 'none');
    body.css('overflow', 'auto');
});

if (document.location.href.indexOf('games') > -1) {
    function resizeTable() {
        $('.product-table tbody').css('width', $('.product-table').width() - 106);
    }

    resizeTable();
    resizeTable();
    resizeTable();
    resizeTable();

    $(window).resize(function () {
        resizeTable();
    });
}

//проверка статуса заказа
function updateStatus() {
    let url = window.location.pathname.replace(/\/orders\//ig, '');

    $.ajax({
        type: "POST",
        url: "/updater",
        data: {
            url: url,
            status: $("main").data("status")
        },
        success: function (data) {
            if (data === "YES") {
                location.reload();
            }
        }
    });
}

if (window.location.pathname.indexOf("orders") === 1) {
    setInterval(updateStatus, 15000);
}