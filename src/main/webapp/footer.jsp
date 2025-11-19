<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <footer class="footer">
        <div class="footer-container">
            <address class="address">
                <p class="titlefooter">KOKYOUNGBIN</p>
                <p>04039 서울특별시 마포구 홍익로3길 20 | 20, Hongik-ro 3-gil, Mapo-gu, Seoul, Republic of Korea</p>
                <p>KORANGEMGMT@GMAIL.COM</p>
            </address>
            <p class="copyright">Copyright &copy; egocci All rights reserved.</p>
        </div>
    </footer>

    <script>
        document.querySelector('.menu-toggle').addEventListener('click', function() {
            document.querySelector('.left-menu').classList.toggle('menu-open');
        });

        function closeMenu(e) {
            if (!e.target.closest('.left-menu')) {
                document.querySelector('.left-menu').classList.remove('menu-open');
            }
        }

        document.addEventListener('click', closeMenu);
        document.addEventListener('touchstart', closeMenu);
    </script>
</body>
</html>