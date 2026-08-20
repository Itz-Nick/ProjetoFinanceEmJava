import { api, ApiError } from './api.js';

let currentTransactions = [];
let currentCategories = [];
let editingTransactionId = null;

const formatCurrency = (value) => {
    return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    }).format(value);
};

const formatDate = (dateString) => {
    const date = new Date(dateString + 'T00:00:00');
    return date.toLocaleDateString('pt-BR');
};

const showToast = (message, type = 'success') => {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = `
        <span class="toast-message">${message}</span>
        <button class="toast-close">&times;</button>
    `;
    toast.querySelector('.toast-close').addEventListener('click', () => toast.remove());
    container.appendChild(toast);
    setTimeout(() => toast.remove(), 5000);
};

const showModal = () => {
    document.getElementById('modal-overlay').classList.add('active');
    document.body.style.overflow = 'hidden';
};

const hideModal = () => {
    document.getElementById('modal-overlay').classList.remove('active');
    document.body.style.overflow = '';
    resetForm();
};

const resetForm = () => {
    document.getElementById('transaction-form').reset();
    document.getElementById('transaction-id').value = '';
    document.getElementById('modal-title').textContent = 'Nova Transação';
    editingTransactionId = null;
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('transaction-date').value = today;
};

const populateCategorySelect = (selectId, categories, includeEmpty = true) => {
    const select = document.getElementById(selectId);
    const currentValue = select.value;
    select.innerHTML = '';
    if (includeEmpty) {
        const emptyOption = document.createElement('option');
        emptyOption.value = '';
        emptyOption.textContent = selectId === 'transaction-category' ? 'Selecione uma categoria' : 'Todas';
        select.appendChild(emptyOption);
    }
    categories.forEach(cat => {
        const option = document.createElement('option');
        option.value = cat.id;
        option.textContent = `${cat.icon || ''} ${cat.name}`.trim();
        select.appendChild(option);
    });
    if (currentValue) {
        select.value = currentValue;
    }
};

const renderTransactions = (transactions) => {
    const tbody = document.getElementById('transactions-body');
    const emptyState = document.getElementById('empty-state');
    const countEl = document.getElementById('transactions-count');

    if (transactions.length === 0) {
        tbody.innerHTML = '';
        emptyState.style.display = 'block';
        countEl.textContent = '0 transações';
        return;
    }

    emptyState.style.display = 'none';
    countEl.textContent = `${transactions.length} transação${transactions.length !== 1 ? 'ões' : ''}`;

    tbody.innerHTML = transactions.map(t => `
        <tr data-id="${t.id}">
            <td>${formatDate(t.transactionDate)}</td>
            <td><span class="type-badge ${t.type.toLowerCase()}">${t.type === 'INCOME' ? 'Receita' : 'Despesa'}</span></td>
            <td>${escapeHtml(t.description)}</td>
            <td>
                <div class="category-cell">
                    ${t.categoryIcon ? `<span class="category-icon">${t.categoryIcon}</span>` : ''}
                    <span>${escapeHtml(t.categoryName || 'Sem categoria')}</span>
                </div>
            </td>
            <td class="amount ${t.type.toLowerCase()}">${t.type === 'INCOME' ? '+' : '-'}${formatCurrency(t.amount)}</td>
            <td>
                <div class="action-buttons">
                    <button class="btn-icon edit-btn" title="Editar" data-id="${t.id}">✏️</button>
                    <button class="btn-icon delete delete-btn" title="Excluir" data-id="${t.id}">🗑️</button>
                </div>
            </td>
        </tr>
    `).join('');

    tbody.querySelectorAll('.edit-btn').forEach(btn => {
        btn.addEventListener('click', () => openEditModal(btn.dataset.id));
    });

    tbody.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', () => confirmDelete(btn.dataset.id));
    });
};

const escapeHtml = (text) => {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
};

const updateDashboard = (dashboard) => {
    document.getElementById('total-income').textContent = formatCurrency(dashboard.totalIncome);
    document.getElementById('total-expense').textContent = formatCurrency(dashboard.totalExpense);
    const balanceEl = document.getElementById('balance');
    balanceEl.textContent = formatCurrency(dashboard.balance);
    balanceEl.style.color = dashboard.balance >= 0 ? 'var(--text-primary)' : 'var(--danger-color)';
};

const loadDashboard = async () => {
    try {
        const dashboard = await api.getDashboard();
        updateDashboard(dashboard);
    } catch (error) {
        console.error('Erro ao carregar dashboard:', error);
    }
};

const loadCategories = async () => {
    try {
        currentCategories = await api.getCategories();
        populateCategorySelect('transaction-category', currentCategories);
        populateCategorySelect('filter-category', currentCategories);
    } catch (error) {
        console.error('Erro ao carregar categorias:', error);
        showToast('Erro ao carregar categorias', 'error');
    }
};

const loadTransactions = async (filters = {}) => {
    try {
        let transactions;
        if (filters.startDate && filters.endDate) {
            if (filters.type) {
                transactions = await api.getTransactionsByTypeAndDateRange(filters.type, filters.startDate, filters.endDate);
            } else {
                transactions = await api.getTransactionsByDateRange(filters.startDate, filters.endDate);
            }
        } else {
            transactions = await api.getTransactions();
        }
        currentTransactions = transactions;
        renderTransactions(transactions);
    } catch (error) {
        console.error('Erro ao carregar transações:', error);
        showToast('Erro ao carregar transações', 'error');
    }
};

const openCreateModal = () => {
    resetForm();
    showModal();
};

const openEditModal = async (id) => {
    try {
        const transaction = await api.getTransaction(id);
        editingTransactionId = id;
        document.getElementById('modal-title').textContent = 'Editar Transação';
        document.getElementById('transaction-id').value = transaction.id;
        document.getElementById('transaction-type').value = transaction.type;
        document.getElementById('transaction-amount').value = transaction.amount;
        document.getElementById('transaction-description').value = transaction.description;
        document.getElementById('transaction-category').value = transaction.categoryId || '';
        document.getElementById('transaction-date').value = transaction.transactionDate;
        document.getElementById('transaction-notes').value = transaction.notes || '';
        showModal();
    } catch (error) {
        console.error('Erro ao carregar transação:', error);
        showToast('Erro ao carregar transação', 'error');
    }
};

const confirmDelete = async (id) => {
    if (!confirm('Tem certeza que deseja excluir esta transação?')) return;
    try {
        await api.deleteTransaction(id);
        showToast('Transação excluída com sucesso', 'success');
        await loadTransactions(getFilters());
        await loadDashboard();
    } catch (error) {
        console.error('Erro ao excluir transação:', error);
        showToast('Erro ao excluir transação', 'error');
    }
};

const handleFormSubmit = async (e) => {
    e.preventDefault();
    const formData = {
        type: document.getElementById('transaction-type').value,
        amount: parseFloat(document.getElementById('transaction-amount').value),
        description: document.getElementById('transaction-description').value.trim(),
        transactionDate: document.getElementById('transaction-date').value,
        categoryId: document.getElementById('transaction-category').value || null,
        notes: document.getElementById('transaction-notes').value.trim() || null
    };

    try {
        if (editingTransactionId) {
            await api.updateTransaction(editingTransactionId, formData);
            showToast('Transação atualizada com sucesso', 'success');
        } else {
            await api.createTransaction(formData);
            showToast('Transação criada com sucesso', 'success');
        }
        hideModal();
        await loadTransactions(getFilters());
        await loadDashboard();
    } catch (error) {
        if (error instanceof ApiError && error.status === 400 && error.details) {
            const messages = Object.values(error.details).join(', ');
            showToast(messages, 'error');
        } else {
            showToast(error.message || 'Erro ao salvar transação', 'error');
        }
    }
};

const getFilters = () => {
    const startDate = document.getElementById('filter-start-date').value;
    const endDate = document.getElementById('filter-end-date').value;
    const type = document.getElementById('filter-type').value;
    const categoryId = document.getElementById('filter-category').value;

    return { startDate, endDate, type, categoryId };
};

const applyFilters = () => {
    loadTransactions(getFilters());
};

const clearFilters = () => {
    document.getElementById('filter-type').value = '';
    document.getElementById('filter-category').value = '';
    document.getElementById('filter-start-date').value = '';
    document.getElementById('filter-end-date').value = '';
    loadTransactions();
};

const initializeApp = async () => {
    await loadCategories();
    await loadDashboard();
    await loadTransactions();

    document.getElementById('btn-new-transaction').addEventListener('click', openCreateModal);
    document.getElementById('btn-first-transaction').addEventListener('click', openCreateModal);
    document.getElementById('modal-close').addEventListener('click', hideModal);
    document.getElementById('btn-cancel').addEventListener('click', hideModal);
    document.getElementById('transaction-form').addEventListener('submit', handleFormSubmit);
    document.getElementById('btn-apply-filters').addEventListener('click', applyFilters);
    document.getElementById('btn-clear-filters').addEventListener('click', clearFilters);

    document.getElementById('modal-overlay').addEventListener('click', (e) => {
        if (e.target === document.getElementById('modal-overlay')) {
            hideModal();
        }
    });

    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && document.getElementById('modal-overlay').classList.contains('active')) {
            hideModal();
        }
    });

    const today = new Date().toISOString().split('T')[0];
    document.getElementById('transaction-date').value = today;
};

document.addEventListener('DOMContentLoaded', initializeApp);