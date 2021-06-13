<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\ContatoController;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\TelefoneController;
use App\Http\Controllers\EnderecoController;

// Public Routes

// Auth Routes
Route::post('/register', [AuthController::class, 'register']);
Route::post('/login', [AuthController::class, 'login']);


// Protected Routes            
Route::group(['middleware' => ['auth:sanctum']], function () {

	// Auth Routes             
    Route::post('/logout', [AuthController::class, 'logout']);

	// Contato Routes
	Route::get('contatos/{user_id}', [ContatoController::class, 'index']);
	Route::post('contatos/{user_id}', [ContatoController::class, 'store']);
	Route::get('show/contatos/{contato_id}', [ContatoController::class, 'show']);
	Route::put('update/contatos/{contato_id}', [ContatoController::class, 'update']);
	Route::delete('delete/contatos/{contato_id}', [ContatoController::class, 'destroy']);

	// Telefone Routes
	Route::resource('telefone', TelefoneController::class)->only('store', 'update', 'destroy');

	// Endereco Routes
	Route::resource('endereco', EnderecoController::class)->only('store', 'update', 'destroy');

});

