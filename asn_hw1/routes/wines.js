/* 
 ***************************************************
 *     APLICACIONES Y SERVICIOS EN LA NUBE         *
 *                   ITESO                         *
 *                                                 * 
 *    Actividad 1: Diseño de un WebService         *
 *    Codigo Base: Alvaro Parres (parres@iteso.mx) * 
 *                                                 * 
 *    Alumno: Mario Contreras                      *
 *    Exp: 705080                                  *
 *                                                 *
 ***************************************************
 *                                                 *
 * Instrucciones: Complete el codigo basado en     * 
 * las indicaciones descritas en el documento      *
 *                                                 *
 ***************************************************
 */
/* 
 * Copyright 2017 Mario Contreras <marioc@nazul.net>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var Wine = require('../models/wine');

//Phase 1
exports.findAll = function (req, res) {
    // Wine is our MongoDB object that provides access to wine data
    Wine.find(function (err, wines) {
        if (err)
            return res.status(500).send(err.message);

        console.log('All Wines Request');
        res.status(200).jsonp(wines);
    });
};

exports.findById = function (req, res) {
    var id = req.params.id;
    Wine.findById(id, function (err, wine) {
        if (err)
            return res.status(500).send(err.message);

        console.log('Retrieving wine: ' + id);
        res.status(200).jsonp(wine);
    });
};

exports.addWine = function (req, res) {
    console.log('Add Wine Request');
    var wine = new Wine(req.body);
    console.log('Wine object created');

    wine.save(function (err) {
        if (err)
            return res.status(500).send(err.message);
        res.status(201).jsonp(wine);
        console.log('Wine object saved');
    });
};

exports.deleteWine = function (req, res) {
    var id = req.params.id;
    var wine = req.body;

    Wine.findById(id, function (err, wine) {
        if (err)
            return res.status(500).send(err.message);

        console.log('Deleting wine: ' + id);

        wine.remove(function (err) {
            if (err)
                return res.status(500).send(err.message);
            res.status(200).jsonp(wine);
            console.log('Wine object removed');
        });
    });
};

exports.updateWine = function (req, res) {
    var id = req.params.id;
    var wine = req.body;

    Wine.findById(id, function (err, wine) {
        if (err)
            return res.status(500).send(err.message);

        console.log('Updating wine: ' + id);
        wine.name = req.body.name;
        wine.year = req.body.year;
        wine.grapes = req.body.grapes;
        wine.country = req.body.country;
        wine.description = req.body.description;

        wine.save(function (err) {
            if (err)
                return res.status(500).send(err.message);
            res.status(200).jsonp(wine);
            console.log('Wine object saved');
        });
    });
};

// EOF
