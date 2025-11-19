document.addEventListener('DOMContentLoaded', function() {
    // 레프트 메뉴 초기화
    const menuToggle = document.querySelector('.menu-toggle');
    if (menuToggle) {
        menuToggle.addEventListener('click', function() {
            const leftMenu = document.querySelector('.left-menu');
            if (leftMenu) {
                leftMenu.classList.toggle('menu-open');
            }
        });
    }

    const menuIcon = document.querySelector('.menu-icon');
    if (menuIcon) {
        menuIcon.addEventListener('click', function(e) {
            e.stopPropagation();
            const leftMenu = document.querySelector('.left-menu');
            if (leftMenu && leftMenu.classList.contains('menu-open')) {
                leftMenu.classList.remove('menu-open');
            }
        });
    }

    function closeLeftMenu(e) {
        const leftMenu = document.querySelector('.left-menu');
        const menuToggle = document.querySelector('.menu-toggle');
        
        if (
            leftMenu && 
            !e.target.closest('.left-menu') && 
            !e.target.closest('.menu-toggle')
        ) {
            leftMenu.classList.remove('menu-open');
        }
    }

    document.addEventListener('click', closeLeftMenu);
    document.addEventListener('touchstart', closeLeftMenu);

    // 이메일 폼 초기화
    const emailForm = document.getElementById('emailForm');
    if (emailForm) {
        emailForm.addEventListener('submit', handleEmailSubmit);
    }

    // 모든 슬라이더 초기화
    initAllSliders();
});

// 다중 슬라이더 관리
const sliders = {};

function initAllSliders() {
    const sliderSections = document.querySelectorAll('.music-slider-section');
    
    sliderSections.forEach((section, index) => {
        const sliderId = `slider-${index}`;
        section.setAttribute('data-slider-id', sliderId);
        
        const track = section.querySelector('.cards-track');
        const prevBtn = section.querySelector('.nav-btn.prev');
        const nextBtn = section.querySelector('.nav-btn.next');
        const indicators = section.querySelector('.indicators');
        const cards = track.querySelectorAll('.music-card');
        
        if (cards.length === 0) return;
        
        sliders[sliderId] = {
            track: track,
            prevBtn: prevBtn,
            nextBtn: nextBtn,
            indicators: indicators,
            cards: cards,
            currentIndex: 0,
            cardsPerView: getCardsPerView(),
            cardWidth: 215
        };
        
        createIndicators(sliderId);
        updateButtons(sliderId);
        
        if (prevBtn) prevBtn.onclick = () => prevSlide(sliderId);
        if (nextBtn) nextBtn.onclick = () => nextSlide(sliderId);
    });
    
    // 윈도우 리사이즈 이벤트
    window.addEventListener('resize', () => {
        Object.keys(sliders).forEach(sliderId => {
            sliders[sliderId].cardsPerView = getCardsPerView();
            createIndicators(sliderId);
            updateButtons(sliderId);
        });
    });
}

function getCardsPerView() {
    if (window.innerWidth <= 480) return 1;
    if (window.innerWidth <= 768) return 2;
    if (window.innerWidth <= 1024) return 3;
    return 4;
}

function createIndicators(sliderId) {
    const slider = sliders[sliderId];
    if (!slider || !slider.indicators) return;
    
    const totalCards = slider.cards.length;
    slider.indicators.innerHTML = '';
    
    const maxIndex = Math.max(0, totalCards - slider.cardsPerView);
    const indicatorCount = maxIndex + 1;
    
    for (let i = 0; i < indicatorCount; i++) {
        const indicator = document.createElement('div');
        indicator.className = i === 0 ? 'indicator active' : 'indicator';
        indicator.onclick = () => goToSlide(sliderId, i);
        slider.indicators.appendChild(indicator);
    }
}

function updateIndicators(sliderId) {
    const slider = sliders[sliderId];
    if (!slider || !slider.indicators) return;
    
    const indicators = slider.indicators.querySelectorAll('.indicator');
    indicators.forEach((indicator, index) => {
        indicator.classList.toggle('active', index === slider.currentIndex);
    });
}

function updateButtons(sliderId) {
    const slider = sliders[sliderId];
    if (!slider) return;
    
    const totalCards = slider.cards.length;
    const maxIndex = Math.max(0, totalCards - slider.cardsPerView);
    
    if (slider.prevBtn) {
        slider.prevBtn.disabled = slider.currentIndex <= 0;
    }
    if (slider.nextBtn) {
        slider.nextBtn.disabled = slider.currentIndex >= maxIndex;
    }
}

function goToSlide(sliderId, index) {
    const slider = sliders[sliderId];
    if (!slider) return;
    
    const totalCards = slider.cards.length;
    const maxIndex = Math.max(0, totalCards - slider.cardsPerView);
    slider.currentIndex = Math.max(0, Math.min(index, maxIndex));
    
    slider.track.style.transform = `translateX(-${slider.currentIndex * slider.cardWidth}px)`;
    
    updateButtons(sliderId);
    updateIndicators(sliderId);
}

function prevSlide(sliderId) {
    const slider = sliders[sliderId];
    if (!slider) return;
    
    if (slider.currentIndex > 0) {
        goToSlide(sliderId, slider.currentIndex - 1);
    }
}

function nextSlide(sliderId) {
    const slider = sliders[sliderId];
    if (!slider) return;
    
    const totalCards = slider.cards.length;
    const maxIndex = Math.max(0, totalCards - slider.cardsPerView);
    if (slider.currentIndex < maxIndex) {
        goToSlide(sliderId, slider.currentIndex + 1);
    }
}

// 이메일 관련 함수들
function copyEmail() {
    const emailElement = document.getElementById("email-address");
    if (!emailElement) {
        console.error('이메일 주소 요소를 찾을 수 없습니다.');
        return;
    }
    
    const email = emailElement.value;
    
    if (navigator.clipboard && navigator.clipboard.writeText) {
        navigator.clipboard.writeText(email).then(() => {
            showCopyMessage();
        }).catch(err => {
            console.error('클립보드 복사 실패:', err);
            fallbackCopyTextToClipboard(email);
        });
    } else {
        fallbackCopyTextToClipboard(email);
    }
}

function fallbackCopyTextToClipboard(text) {
    const textArea = document.createElement("textarea");
    textArea.value = text;
    textArea.style.position = "fixed";
    textArea.style.left = "-999999px";
    textArea.style.top = "-999999px";
    
    document.body.appendChild(textArea);
    textArea.focus();
    textArea.select();
    
    try {
        const successful = document.execCommand('copy');
        if (successful) {
            showCopyMessage();
        } else {
            alert('복사하기 기능을 사용할 수 없습니다.');
        }
    } catch (err) {
        console.error('복사 실패:', err);
        alert('복사하기 기능을 사용할 수 없습니다.');
    }
    
    document.body.removeChild(textArea);
}

function showCopyMessage() {
    const msg = document.getElementById("copy-msg");
    if (msg) {
        msg.style.display = "block";
        setTimeout(() => {
            msg.style.display = "none";
        }, 2000);
    }
}

function handleEmailSubmit(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    
    fetch('send_email.php', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data && data.status === 'success') {
            alert('메일이 성공적으로 전송되었습니다!');
            const emailToggle = document.getElementById('email-toggle');
            if (emailToggle) {
                emailToggle.checked = false;
            }
            e.target.reset();
        } else {
            alert('메일 전송 중 오류가 발생했습니다.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('메일 전송 기능이 현재 사용할 수 없습니다.');
    });
}