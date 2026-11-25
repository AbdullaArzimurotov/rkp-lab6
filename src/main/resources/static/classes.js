document.querySelectorAll('[data-school-select]').forEach((school) => {
    const classes = school.form.querySelector('[data-class-select]');
    if (!classes) return;

    function updateClasses() {
        for (const option of classes.options) {
            if (!option.value) continue;
            const visible = !school.value || option.dataset.school === school.value;
            option.hidden = !visible;
            option.disabled = !visible;
        }
        if (classes.selectedOptions[0]?.disabled) classes.value = '';
    }

    school.addEventListener('change', updateClasses);
    updateClasses();
});
