document.addEventListener('DOMContentLoaded', function() {
    // Form validation
    const forms = document.querySelectorAll('form[id$="Form"]');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            const pass = form.querySelector('input[type="password"]');
            if (pass && pass.value.length < 6) {
                alert('Password must be at least 6 characters!');
                e.preventDefault();
            }
        });
    });

    // Dashboard token check
    if (window.location.pathname === '/dashboard' && !localStorage.getItem('token')) {
        window.location.href = '/login?invalid';
    }

    // Responsive adjustment (optional)
    window.addEventListener('resize', () => {
        // Tailwind handles, but custom if needed
    });
});