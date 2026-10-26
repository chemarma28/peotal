// categories.Category
export interface Category {color: string; icon?: string; id?: number; name: string; operation: TransactionType; ordinal: number;}
// transactions.Currency
export enum Currency {BYN = 'BYN', RUB = 'RUB', USD = 'USD', EUR = 'EUR'}
// transactions.Transaction
export interface Transaction {amount: number; categoryId: number; currency: Currency; description?: string; id?: number; issued: string; operation: TransactionType;}
// transactions.TransactionType
export enum TransactionType {EXPENSE = 'EXPENSE', INCOME = 'INCOME'}
