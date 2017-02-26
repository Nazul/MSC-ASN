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

var records;
var currentPage;

$(document).ready(function () {
    $("#version").html("v0.14");

    $("#searchbutton").click(function (e) {
        displayModal();
    });

    $("#searchfield").keydown(function (e) {
        if (e.keyCode == 13) {
            displayModal();
        }
    });

    function displayModal() {
        $("#myModal").modal('show');
        $("#status").html("Searching...");
        $("#dialogtitle").html("Search for: " + $("#searchfield").val());
        $("#previous").hide();
        $("#next").hide();
        $.getJSON('/search/' + $("#searchfield").val(), function (data) {
            renderQueryResults(data);
        });
    }
    
    function displayImgs() {
        for(var i = 4; i > 0; i--) {
            var max = currentPage * 4;
            if (records.results[max - i] !== undefined) {
                $("#photo" + (4 - i)).html("<img src=\"" + records.results[max - i] + "\" style=\"max-width: 100px; max-height: 100px\" />");
            }
            else {
                $("#photo" + (4 - i)).html("&nbsp;");
            }
        }
    }

    $("#next").click(function (e) {
        //e.preventDefault();
        currentPage++;
        console.log("Current Page: " + currentPage + "\n");
        if ((currentPage * 4) > records.num_results) {
            $("#next").hide();
        }
        $("#previous").show();
        displayImgs();
    });

    $("#previous").click(function (e) {
        //e.preventDefault();
        currentPage--;
        console.log("Current Page: " + currentPage + "\n");
        if ((currentPage * 4) <= 4) {
            $("#previous").hide();
        }
        $("#next").show();
        displayImgs();
    });

    function renderQueryResults(data) {
        if (data.error !== undefined) {
            $("#status").html("Error: " + data.error);
        } else {
            records = data;
            currentPage = 1;
            console.log("Current Page: " + currentPage + "\n");
            $("#status").html("" + data.num_results + " result(s)");
            if (data.num_results > 4) {
                $("#next").show();
            }
            displayImgs();
        }
    }
});

// EOF
