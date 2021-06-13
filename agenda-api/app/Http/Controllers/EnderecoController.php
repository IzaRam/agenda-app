<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Endereco;
use App\Models\Contato;

class EnderecoController extends Controller
{

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
		$request->validate([
			"contato_id" => "required|integer",
			"cep" => "required|string",
			"cidade" => "required|string",
			"uf" => "required|string",
		]);
		Contato::findOrFail($request['contato_id']);
		return response()->json(Endereco::create($request->all()), 201);

    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
		$request->validate([
			"contato_id" => "sometimes|prohibited"
		]);
		$endereco =Endereco::findOrFail($id);
		$endereco->update($request->all());
		return response()->json($endereco);

    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
		Endereco::findOrFail($id);
		return response()->json(Endereco::destroy($id));
    }
}
