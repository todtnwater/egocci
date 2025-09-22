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

    // 메뉴 아이콘 직접 클릭으로도 닫기 가능하도록
    const menuIcon = document.querySelector('.menu-icon');
    if (menuIcon) {
        menuIcon.addEventListener('click', function(e) {
            e.stopPropagation(); // 이벤트 버블링 방지
            const leftMenu = document.querySelector('.left-menu');
            if (leftMenu && leftMenu.classList.contains('menu-open')) {
                leftMenu.classList.remove('menu-open');
            }
        });
    }

    // 메뉴 닫기 함수 (레프트 메뉴만 처리)
    function closeLeftMenu(e) {
        const leftMenu = document.querySelector('.left-menu');
        const menuToggle = document.querySelector('.menu-toggle');
        
        // left-menu 바깥 클릭 && 메뉴 토글 버튼이 아닌 경우에만 닫기
        if (
            leftMenu && 
            !e.target.closest('.left-menu') && 
            !e.target.closest('.menu-toggle')
        ) {
            leftMenu.classList.remove('menu-open');
        }
    }

    // 레프트 메뉴 닫기 이벤트만 등록
    document.addEventListener('click', closeLeftMenu);
    document.addEventListener('touchstart', closeLeftMenu);

    // 이메일 폼 초기화
    const emailForm = document.getElementById('emailForm');
    if (emailForm) {
        emailForm.addEventListener('submit', handleEmailSubmit);
    }

    // 슬라이더 초기화
    initSlider();
});

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

// 개선된 이메일 복사 함수
function copyEmail() {
    const emailElement = document.getElementById("email-address");
    if (!emailElement) {
        console.error('이메일 주소 요소를 찾을 수 없습니다.');
        return;
    }
    
    const email = emailElement.value;
    
    // navigator.clipboard API 지원 확인 (HTTPS 환경 필요)
    if (navigator.clipboard && navigator.clipboard.writeText) {
        navigator.clipboard.writeText(email).then(() => {
            showCopyMessage();
        }).catch(err => {
            console.error('클립보드 복사 실패:', err);
            fallbackCopyTextToClipboard(email);
        });
    } else {
        // 대체 방법 사용 (HTTP 환경에서 작동)
        fallbackCopyTextToClipboard(email);
    }
}

// 대체 복사 방법
function fallbackCopyTextToClipboard(text) {
    const textArea = document.createElement("textarea");
    textArea.value = text;
    
    // 화면 밖으로 이동
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
            alert('복사하기 기능을 사용할 수 없습니다. 이메일 주소를 직접 선택해서 복사해주세요.');
        }
    } catch (err) {
        console.error('복사 실패:', err);
        alert('복사하기 기능을 사용할 수 없습니다.');
    }
    
    document.body.removeChild(textArea);
}

// 복사 완료 메시지 표시
function showCopyMessage() {
    const msg = document.getElementById("copy-msg");
    if (msg) {
        msg.style.display = "block";
        setTimeout(() => {
            msg.style.display = "none";
        }, 2000);
    }
}

// 이메일 폼 전송 처리
function handleEmailSubmit(e) {
    e.preventDefault();
    
    const formData = new FormData(e.target);
    
    // send_email.php 파일이 존재하는지 확인 필요
    fetch('send_email.php', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
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
        alert('메일 전송 기능이 현재 사용할 수 없습니다. 직접 이메일로 연락해주세요.');
    });
}