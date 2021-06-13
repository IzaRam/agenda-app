<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Telefone;
use App\Models\Contato;

class TelefoneController extends Controller
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
			"numero" => "required|string",
			"tipo" => "required|string",
			"contato_id" => "required|integer"
		]);
		Contato::findOrFail($request['contato_id']);
		return response()->json(Telefone::create($request->all()), 201);
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
			"numero" => "sometimes|required|string",
			"tipo" => "sometimes|required|string",
			"contato_id" => "sometimes|prohibited"
		]);
		$telefone = Telefone::findOrFail($id);
		$telefone->update($request->all());
		return response()->json($telefone);
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
		Telefone::findOrFail($id);
		return response()->json(Telefone::destroy($id));
    }
}
