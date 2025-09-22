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

    // 메뉴 닫기 함수
    function closeMenu(e) {
        const leftMenu = document.querySelector('.left-menu');
        if (leftMenu && !e.target.closest('.left-menu')) {
            leftMenu.classList.remove('menu-open');
        }
    }

    document.addEventListener('click', closeMenu);
    document.addEventListener('touchstart', closeMenu);

    // 슬라이더 초기화
    initSlider();
});

// 모바일 터치 이벤트 추가
document.addEventListener('click', function(e) {
    if (!e.target.closest('.left-menu')) {
        document.querySelector('.left-menu').classList.remove('menu-open');
    }
});

document.addEventListener('touchstart', function(e) {
    if (!e.target.closest('.left-menu')) {
        document.querySelector('.left-menu').classList.remove('menu-open');
    }
});

// 또는 통합 버전
function closeMenu(e) {
    if (!e.target.closest('.left-menu')) {
        document.querySelector('.left-menu').classList.remove('menu-open');
    }
}

document.addEventListener('click', closeMenu);
document.addEventListener('touchstart', closeMenu);

// 슬라이더 변수들
let currentIndex = 0;
let cardsPerView = 3;
const cardWidth = 215; // 카드 너비 + 간격

// 화면 크기에 따른 카드 개수 설정
function getCardsPerView() {
    if (window.innerWidth <= 480) return 1;
    if (window.innerWidth <= 768) return 2;
    if (window.innerWidth <= 1024) return 3;
    return 4;
}

// 인디케이터 생성
function createIndicators() {
    const indicators = document.getElementById('indicators');
    if (!indicators) return;
    
    const cards = document.querySelectorAll('.music-card');
    const totalCards = cards.length;
    
    if (totalCards === 0) return;
    
    indicators.innerHTML = '';
    
    const maxIndex = Math.max(0, totalCards - cardsPerView);
    const indicatorCount = maxIndex + 1;
    
    for (let i = 0; i < indicatorCount; i++) {
        const indicator = document.createElement('div');
        indicator.className = i === 0 ? 'indicator active' : 'indicator';
        indicator.onclick = () => goToSlide(i);
        indicators.appendChild(indicator);
    }
}

// 인디케이터 업데이트
function updateIndicators() {
    const indicators = document.querySelectorAll('.indicator');
    indicators.forEach((indicator, index) => {
        indicator.classList.toggle('active', index === currentIndex);
    });
}

// 버튼 상태 업데이트
function updateButtons() {
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    
    if (!prevBtn || !nextBtn) return;
    
    const cards = document.querySelectorAll('.music-card');
    const totalCards = cards.length;
    
    if (totalCards === 0) {
        prevBtn.disabled = true;
        nextBtn.disabled = true;
        return;
    }
    
    const maxIndex = Math.max(0, totalCards - cardsPerView);
    
    prevBtn.disabled = currentIndex <= 0;
    nextBtn.disabled = currentIndex >= maxIndex;
}

// 슬라이드 이동
function goToSlide(index) {
    const cards = document.querySelectorAll('.music-card');
    const totalCards = cards.length;
    
    if (totalCards === 0) return;
    
    const maxIndex = Math.max(0, totalCards - cardsPerView);
    currentIndex = Math.max(0, Math.min(index, maxIndex));
    
    const track = document.getElementById('cardsTrack');
    if (track) {
        track.style.transform = `translateX(-${currentIndex * cardWidth}px)`;
    }
    
    updateButtons();
    updateIndicators();
}

// 이전 슬라이드
function prevSlide() {
    if (currentIndex > 0) {
        goToSlide(currentIndex - 1);
    }
}

// 다음 슬라이드
function nextSlide() {
    const cards = document.querySelectorAll('.music-card');
    const totalCards = cards.length;
    
    if (totalCards === 0) return;
    
    const maxIndex = Math.max(0, totalCards - cardsPerView);
    if (currentIndex < maxIndex) {
        goToSlide(currentIndex + 1);
    }
}

// 슬라이더 초기화
function initSlider() {
    cardsPerView = getCardsPerView();
    createIndicators();
    updateButtons();
    
    // 버튼 이벤트 연결
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    
    if (prevBtn) prevBtn.onclick = prevSlide;
    if (nextBtn) nextBtn.onclick = nextSlide;
    
    // 키보드 이벤트
    document.addEventListener('keydown', (e) => {
        if (e.key === 'ArrowLeft') prevSlide();
        if (e.key === 'ArrowRight') nextSlide();
    });
    
    // 윈도우 리사이즈 이벤트
    window.addEventListener('resize', () => {
        cardsPerView = getCardsPerView();
        createIndicators();
        updateButtons();
        updateIndicators();
    });
}

// 페이지 로드 후 초기화
document.addEventListener('DOMContentLoaded', initSlider);


// 이메일 폼 전송 처리 (AJAX)
document.getElementById('emailForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const formData = new FormData(this);
    
    fetch('send_email.php', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 'success') {
            alert('메일이 성공적으로 전송되었습니다!');
            document.getElementById('email-toggle').checked = false;
            this.reset();
        } else {
            alert('메일 전송 중 오류가 발생했습니다.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('메일 전송 중 오류가 발생했습니다.');
    });
});

// 패널 외부 클릭시 닫기
document.addEventListener('click', function(e) {
    const contactMenu = document.querySelector('.right-contact-menu');
    const emailToggle = document.getElementById('email-toggle');
    
    if (emailToggle.checked && !contactMenu.contains(e.target)) {
        emailToggle.checked = false;
    }
});