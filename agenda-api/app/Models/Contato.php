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
		'categoria',
		'user_id'
    ];

	function telefones() {
		return $this->hasMany('App\Models\Telefone');
	}

	function enderecos() {
		return $this->hasMany('App\Models\Endereco');
	}
}
