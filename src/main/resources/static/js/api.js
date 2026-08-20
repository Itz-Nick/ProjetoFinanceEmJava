const API_BASE = '/api';

class ApiError extends Error {
    constructor(message, status, details) {
        super(message);
        this.name = 'ApiError';
        this.status = status;
        this.details = details;
    }
}

async function handleResponse(response) {
    if (!response.ok) {
        let errorData;
        try {
            errorData = await response.json();
        } catch {
            errorData = { message: response.statusText };
        }
        throw new ApiError(
            errorData.message || 'Erro na requisição',
            response.status,
            errorData.details
        );
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}

export const api = {
    // Transações
    async getTransactions() {
        const response = await fetch(`${API_BASE}/transactions`);
        return handleResponse(response);
    },

    async getTransaction(id) {
        const response = await fetch(`${API_BASE}/transactions/${id}`);
        return handleResponse(response);
    },

    async createTransaction(data) {
        const response = await fetch(`${API_BASE}/transactions`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return handleResponse(response);
    },

    async updateTransaction(id, data) {
        const response = await fetch(`${API_BASE}/transactions/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return handleResponse(response);
    },

    async deleteTransaction(id) {
        const response = await fetch(`${API_BASE}/transactions/${id}`, {
            method: 'DELETE'
        });
        return handleResponse(response);
    },

    async getTransactionsByDateRange(startDate, endDate) {
        const params = new URLSearchParams({
            startDate: startDate.toISOString().split('T')[0],
            endDate: endDate.toISOString().split('T')[0]
        });
        const response = await fetch(`${API_BASE}/transactions/filter?${params}`);
        return handleResponse(response);
    },

    async getTransactionsByTypeAndDateRange(type, startDate, endDate) {
        const params = new URLSearchParams({
            startDate: startDate.toISOString().split('T')[0],
            endDate: endDate.toISOString().split('T')[0]
        });
        const response = await fetch(`${API_BASE}/transactions/type/${type}?${params}`);
        return handleResponse(response);
    },

    async getDashboard() {
        const response = await fetch(`${API_BASE}/transactions/dashboard`);
        return handleResponse(response);
    },

    async getCategorySummary(startDate, endDate) {
        const params = new URLSearchParams({
            startDate: startDate.toISOString().split('T')[0],
            endDate: endDate.toISOString().split('T')[0]
        });
        const response = await fetch(`${API_BASE}/transactions/category-summary?${params}`);
        return handleResponse(response);
    },

    // Categorias
    async getCategories() {
        const response = await fetch(`${API_BASE}/categories`);
        return handleResponse(response);
    },

    async getDefaultCategories() {
        const response = await fetch(`${API_BASE}/categories/defaults`);
        return handleResponse(response);
    },

    async createCategory(data) {
        const response = await fetch(`${API_BASE}/categories`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return handleResponse(response);
    },

    async updateCategory(id, data) {
        const response = await fetch(`${API_BASE}/categories/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return handleResponse(response);
    },

    async deleteCategory(id) {
        const response = await fetch(`${API_BASE}/categories/${id}`, {
            method: 'DELETE'
        });
        return handleResponse(response);
    },

    async initializeDefaults() {
        const response = await fetch(`${API_BASE}/categories/initialize-defaults`, {
            method: 'POST'
        });
        return handleResponse(response);
    }
};

export { ApiError };