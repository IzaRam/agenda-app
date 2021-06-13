<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Contato;
use App\Models\Endereco;
use App\Models\Telefone;

class ContatoController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index($id)
    {
		return response()->json(Contato::where('user_id', $id)->get());
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
		$request->validate([
			"nome" => "required|string",
			"categoria" => "required|string",
			"user_id" => "required|integer"
		]);
		return response()->json(Contato::create($request->all()));
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        $contato = Contato::findOrFail($id);
		$contato->enderecos;
		$contato->telefones;
		return response()->json($contato);
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
			"nome" => "sometimes|required|string",
			"categoria" => "sometimes|required|string",
			"user_id" => "sometimes|required|integer"
		]);
        $contato = Contato::findOrFail($id);
		$contato->update($request->all());
		return response()->json($contato);
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
		$contato = Contato::findOrFail($id);
		foreach($contato->telefones as $telefone) {
			Telefone::destroy($telefone->id);
		}
		foreach($contato->enderecos as $endereco) {
			Endereco::destroy($endereco->id);
		}
		return response()->json(Contato::destroy($id));
    }
}
