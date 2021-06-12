<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Contato extends Model
{
    use HasFactory;

	/**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'nome',
        'categoria'
    ];

	function getTelefones() {
		return $this->hasMany('App\Models\Telefone');
	}

	function getEnderecos() {
		return $this->hasMany('App\Models\Endereco');
	}
}
